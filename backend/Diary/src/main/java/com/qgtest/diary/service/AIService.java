package com.qgtest.diary.service;

import com.qgtest.diary.entity.AiAnylize;
import com.qgtest.diary.mapper.AiAnylizeMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class AIService {
    private final ChatClient chatClient;

    @Autowired
    AiAnylizeMapper aiAnylizeMapper;
    public AIService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }
    // 普通对话
    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
    // 生成笔记摘要
    public String generateSummary(String noteContent) {
        return chatClient.prompt()
                .system("你是专业的笔记摘要生成助手")
                .user("请为以下笔记生成摘要（50字以内）\n" +
                        "笔记内容: \n"+noteContent)
                .call()
                .content();
    }
    public String generateKeyPoint(String noteContent) {
        return chatClient.prompt()
                .system("你是专业的笔记要点生成助手")
                .user("请为以下笔记提取3-5个要点，每点15字以内，用逗号分隔：\n" +
                        "笔记内容: \n"+noteContent)
                .call()
                .content();
    }
    public String generateTags(String noteContent) {
        return chatClient.prompt()
                .system("你是专业的笔记标签生成助手")
                .user("请为以下笔记推荐3-5个标签，用逗号分隔：\n" +
                        "笔记内容: \n"+noteContent)
                .call()
                .content();
    }
    //后续可以做一下流式传输
    //查询ai分析结果
    public AiAnylize selectByNoteId(Long noteId){
        return aiAnylizeMapper.selectByNoteId(noteId);
    }
    //保存/更新ai分析结果
    public void saveAnalysis(Long noteId, String summary, String key, String tags){
        AiAnylize aiAnylize = aiAnylizeMapper.selectByNoteId(noteId);
        if(aiAnylize != null) {
            aiAnylize.setSummary(summary);
            aiAnylize.setKeyPoint(key);
            aiAnylize.setTags(tags);
            aiAnylizeMapper.updateByNoteId(aiAnylize);
            return;
        }
        aiAnylize = new AiAnylize();
        aiAnylize.setNoteId(noteId);
        aiAnylize.setSummary(summary);
        aiAnylize.setTags(tags);
        aiAnylize.setKeyPoint(key);
        aiAnylizeMapper.insert(aiAnylize);
    }
    //删除+批量删除
    public void deleteByNoteId(Long noteId){
        aiAnylizeMapper.deleteByNoteId(noteId);
    }
    public void deleteBatchByNoteIds(List<Long> noteIds){
        if (noteIds != null && !noteIds.isEmpty()) {
            aiAnylizeMapper.deleteBatchByNoteIds(noteIds);
        }
    }
}
