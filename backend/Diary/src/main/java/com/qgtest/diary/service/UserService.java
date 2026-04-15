package com.qgtest.diary.service;

import com.qgtest.diary.common.BizException;
import com.qgtest.diary.common.Result;
import com.qgtest.diary.entity.AiAnylize;
import com.qgtest.diary.entity.Friendship;
import com.qgtest.diary.entity.Note;
import com.qgtest.diary.entity.User;
import com.qgtest.diary.mapper.AiAnylizeMapper;
import com.qgtest.diary.mapper.FriendshipMapper;
import com.qgtest.diary.mapper.NoteMapper;
import com.qgtest.diary.mapper.UserMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.DrbgParameters;
import java.security.spec.ECField;
import java.text.Format;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    //图片保存路径
    @Value("${upload.path}")
    private String uploadPath;
    //服务器路径--也就是说前端要先访问后端host然后再根据上面的路径拿到头像图片
    @Value("${server.base_url}")
    private String baseUrl;
    @Autowired
    UserMapper userMapper;
    @Autowired
    NoteMapper noteMapper;
    @Autowired
    FriendshipMapper friendshipMapper;
    @Autowired
    AiAnylizeMapper aiAnylizeMapper;
    @Autowired
    AIService aiService;
    public static final Pattern EMAIL = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    //加密工具，"SHA-256"的256位存储
    public static class EncryptUtil{
        public static String encrypt(String input){
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(input.getBytes());
                StringBuilder hex = new StringBuilder();
                for(byte b : hash){
                    hex.append(String.format("%02x",b));
                }
                return hex.toString();
            }catch (Exception e){
                return input;
            }
        }
        public static boolean check(String pwd,String encrypted){return encrypt(pwd).equals(encrypted);}
    }
    //用户操作部分-----------------------------------------------------------------------
    //登录、在controller中返回token
    public User login(String keyword,String password){
        if(keyword == null || keyword.isEmpty()){
            throw new BizException(401,"账号问空");
        }
        User user = new User();
        //查找用户
        if(keyword.contains("@")){
            user = userMapper.selectDerectByEmail(keyword);
            if(user == null){throw new BizException(401,"用户未注册");}
        }else{
            user = userMapper.selectDrectByPhone(keyword);
            if(user == null){throw new BizException(401,"用户未注册");}
        }
        //密码校验
        if(!EncryptUtil.check(password,user.getPassword())){
            throw new BizException(401,"密码错误");
        }
        return  user;
    }
    //注册
    @Transactional
    public void register( String account , String email,String phone,String password,String confirmPwd){
        if(email!=null && !email.isEmpty()){//邮箱校验
            if(!EMAIL.matcher(email).matches()){
                throw new BizException(401,"邮箱格式错误");
            }
            User existUser = userMapper.selectDerectByEmail(email);
            if(existUser !=null){
                throw new BizException("邮箱已被注册");
            }
        }else{
            throw new BizException(401,"邮箱不能为空");
        }
        if(phone!=null || !phone.isEmpty()){//邮箱校验
            User existUser = userMapper.selectDrectByPhone(phone);
            if(existUser !=null){
                throw new BizException("手机号已被注册");
            }
        }else{
            throw new BizException(401,"手机号不能为空");
        }
        if(account == null || account.isEmpty()){throw new BizException(401,"账号不能为空");}
        if(!password.equals(confirmPwd)){throw new BizException(401,"确认密码错误");}
        String encrypted = EncryptUtil.encrypt(password);
        User user = new User();
        user.setAccount(account);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(encrypted);
        userMapper.insert(user);
    }
    //修改座右铭
    @Transactional
    public void resetMotto(String motto,long userId){
        User user = userMapper.selectById(userId);
        user.setMotto(motto);
        userMapper.update(user);
    }
    //修改头像，这里没有做删除上一头像，更新头像多了可能会变成石山
    @Transactional
    public String updateAvatar(Long userId, MultipartFile file) {
        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BizException("错误的图片格式");
        }

        //  生成文件名（ 格式：userId_时间戳.扩展名）
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));//获取拓展名，因为用户传的不一定是同一格式的图片
        String fileName = userId + "_" + System.currentTimeMillis() + ext;

        // 保存文件到本地
        File dest = new File(uploadPath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //这里会抛IO异常
        try {
            file.transferTo(dest);
        }catch (Exception e){
            throw new BizException("头像上传失败"+e.getMessage());
        }
        // 4. 返回访问URL
        String avatarUrl = baseUrl + "/uploads/avatars/" + fileName;

        // 5. 更新数据库
        User user = userMapper.selectById(userId);
        user.setAvatar(avatarUrl);
        userMapper.update(user);

        return avatarUrl;
    }
    //修改密码
    @Transactional
    public void resetPassword(long userId,String oldPwd,String newPwd){
        User user = userMapper.selectById(userId);
        if(!EncryptUtil.encrypt(oldPwd).equals(user.getPassword())){
            throw new BizException("原始密码错误");
        }
        String encrypted = EncryptUtil.encrypt(newPwd);
        user.setPassword(encrypted);
        userMapper.update(user);
    }
    //好友部分---------------------------------------------------------------
    //查询所有好友，搜索好友（邮箱/手机号）模糊搜索，查询是否有好友请求,添加好友，同意/拒绝,删除好友
    @Transactional
    public List<User> findAllFriends(long userId){
        return friendshipMapper.selectAllFriends(userId);
    }
    //查询用户，后面controller别忘了用mapper里查关系的方法给已经是好友的用户加个标识
    @Transactional
    public List<User> findUserByKeyWord(String keyWord){
        return userMapper.searchUsers(keyWord);
    }
    //查找所有待同意请求
    @Transactional
    public List<Friendship> findAllWaittingRequest(long userId){
        return friendshipMapper.selectAllRequests(userId,0);
    }
    //已同意请求
    @Transactional
    public List<Friendship> findAllAcceptedRequest(long userId){
        return friendshipMapper.selectAllRequests(userId,1);
    }
    //被拒绝请求
    @Transactional
    public List<Friendship> findAllRejectedRequest(long userId){
        return friendshipMapper.selectAllRequests(userId,2);
    }
    //更改好友状态/申请状态
    @Transactional
    public void resetFriendRequestState(long userId,long friendId,int state){
        friendshipMapper.update(userId,friendId,state);
    }
    @Transactional
    public void sendFriendRequest(long userId,long friendId){
        // 不能添加自己为好友
        if(userId == friendId){
            throw new BizException("不能添加自己为好友");
        }

        // 检查目标用户是否存在
        User targetUser = userMapper.selectById(friendId);
        if(targetUser == null){
            throw new BizException("目标用户不存在");
        }

        // 检查是否已经是好友关系（双向检查）
        Friendship existingRelation1 = friendshipMapper.selectById(userId, friendId);
        Friendship existingRelation2 = friendshipMapper.selectById(friendId, userId);

        if(existingRelation1 != null || existingRelation2 != null){
            Friendship existing = existingRelation1 != null ? existingRelation1 : existingRelation2;

            // 根据状态给出不同提示
            if(existing.getState() == 1){
                throw new BizException("你们已经是好友了");
            }else if(existing.getState() == 0){
                throw new BizException("好友申请已发送，等待对方同意");
            }else if(existing.getState() == 2||existing.getState() == 3){
                existing.setState(0);
                friendshipMapper.update(existing.getUserId(),existing.getFriendId(),existing.getState());
            }
        }else{
            Friendship friendship = new Friendship(userId,friendId);
            friendshipMapper.insert(friendship);
        }
    }
    //笔记操作部分------------------------------------------------------------
    //查询所有私有笔记，查询所有公开笔记,添加笔记，删除笔记，更新笔记，更新标签,更改笔记可见性
    @Transactional
    public List<Note> findMyNote(long userId){
        return noteMapper.selectByUserId(userId);
    }
    @Transactional
    public List<Note> findAllVisibleNote(){
        return noteMapper.selectAllVisibleNote();
    }
    @Transactional
    public void addNote(long userId,String content,String tags){
        Note note = new Note(userId,content,tags);
        noteMapper.insert(note);
    }
    //回收站
    @Transactional
    public void trashNote(long noteId){
        noteMapper.trashById(noteId,1);
    }
    //取出
    @Transactional
    public void cancelTrashNote(long noteId){
        noteMapper.trashById(noteId,0);
    }
    @Transactional
    public void deleteNote(Long noteId) {
        //删除笔记
        noteMapper.deleteById(noteId);

        //删除关联的AI分析
        aiAnylizeMapper.deleteByNoteId(noteId);
    }
    //更新
    public void updateNoteContent(long noteId,String content){
        Note note = noteMapper.selectById(noteId);
        note.setContent(content);
        noteMapper.update(note);
    }
}
