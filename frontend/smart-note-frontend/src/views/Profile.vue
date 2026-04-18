<template>
  <div class="min-h-screen bg-[#fafafa] p-8">
    <div class="max-w-4xl mx-auto space-y-6">
      
      <div class="flex items-center justify-between mb-8">
        <div class="flex items-center gap-4">
          <button @click="$router.back()" class="p-2 text-gray-400 hover:bg-gray-200 rounded-full transition-all"><ArrowLeft class="w-5 h-5" /></button>
          <h1 class="text-2xl font-bold text-gray-800">个人中心</h1>
        </div>
        <button @click="logout" class="flex items-center gap-2 px-4 py-2 text-sm text-gray-600 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all">
          <LogOut class="w-4 h-4" /> 退出登录
        </button>
      </div>

      <div class="bg-white rounded-[1rem] p-8 shadow-sm border border-gray-100">
        <h2 class="text-lg font-bold text-gray-800 mb-6">基本资料</h2>
        <div class="flex gap-8 items-start">
          <div class="relative group cursor-pointer" @click="triggerAvatarUpload">
            <img :src="userInfo.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + (userInfo.account || 'default')" class="w-24 h-24 rounded-full ring-4 ring-gray-50 object-cover" />
            <div class="absolute inset-0 bg-black/40 rounded-full opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
              <Camera class="w-6 h-6 text-white" />
            </div>
            <input ref="avatarInput" type="file" accept="image/*" class="hidden" @change="handleAvatarChange" />
          </div>
          <div class="flex-1 space-y-4">
            <div class="flex items-center gap-4">
              <label class="w-16 text-sm text-gray-500 text-right">昵称</label>
              <input v-model="editForm.username" type="text" class="flex-1 bg-transparent border-b border-gray-200 focus:border-indigo-500 py-1 text-sm outline-none transition-colors" placeholder="请输入昵称" />
              <Edit2 class="w-4 h-4 text-gray-400" />
            </div>
            <div class="flex items-center gap-4">
              <label class="w-16 text-sm text-gray-500 text-right">座右铭</label>
              <input v-model="editForm.motto" type="text" class="flex-1 bg-transparent border-b border-gray-200 focus:border-indigo-500 py-1 text-sm outline-none transition-colors" placeholder="请输入座右铭" />
              <Edit2 class="w-4 h-4 text-gray-400" />
            </div>
            <div class="flex items-center gap-4">
              <label class="w-16 text-sm text-gray-500 text-right">邮箱</label>
              <span class="text-sm text-gray-700">{{ userInfo.email || '未设置' }}</span>
            </div>
            <div class="flex items-center gap-4">
              <label class="w-16 text-sm text-gray-500 text-right">手机号</label>
              <span class="text-sm text-gray-700">{{ userInfo.phone || '未设置' }}</span>
            </div>
            <div class="pt-4 ml-20">
              <button @click="saveProfile" :disabled="saving" class="bg-indigo-600 text-white px-6 py-2 rounded-full text-sm font-medium hover:bg-indigo-700 active:scale-95 transition-all shadow-sm disabled:opacity-50">
                {{ saving ? '保存中...' : '保存修改' }}
              </button>
              <button @click="showPasswordModal = true" class="ml-4 text-sm text-indigo-600 hover:underline">修改密码</button>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-[1rem] p-8 shadow-sm border border-gray-100">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-lg font-bold text-gray-800">我的好友</h2>
          <div class="flex gap-3">
            <div class="relative">
              <Search class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
              <input v-model="searchKeyword" @keyup.enter="searchUser" type="text" placeholder="搜索邮箱/手机号" class="bg-gray-50 border-none rounded-full py-1.5 pl-9 pr-4 text-sm focus:ring-2 focus:ring-indigo-500/20" />
            </div>
            <button @click="searchUser" class="bg-indigo-50 text-indigo-600 px-4 py-1.5 rounded-full text-sm font-medium hover:bg-indigo-100 transition-colors">搜索</button>
          </div>
        </div>

        <div v-if="searchResult" class="mb-6 bg-blue-50 rounded-xl p-4 border border-blue-100 flex justify-between items-center">
          <div class="flex items-center gap-3">
            <img :src="searchResult.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + searchResult.account" class="w-10 h-10 rounded-full" />
            <div>
              <p class="text-sm font-bold text-gray-800">{{ searchResult.username || searchResult.account }}</p>
              <p class="text-xs text-gray-500">{{ searchResult.email || searchResult.phone }}</p>
            </div>
          </div>
          <button @click="sendFriendRequest(searchResult.id)" class="px-4 py-1.5 rounded-full text-xs font-medium text-white bg-indigo-600 hover:bg-indigo-700 shadow-sm">添加好友</button>
        </div>

        <div v-if="friendRequests.length > 0" class="mb-6 space-y-3">
          <h3 class="text-sm font-semibold text-gray-600 mb-3">好友请求</h3>
          <div v-for="req in friendRequests" :key="req.id" class="bg-orange-50 rounded-xl p-4 border border-orange-100 flex justify-between items-center">
            <div class="flex items-center gap-3">
              <img :src="req.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + req.account" class="w-10 h-10 rounded-full" />
              <div>
                <p class="text-sm font-bold text-gray-800">{{ req.username || req.account }} <span class="text-xs font-normal text-gray-500 ml-2">请求添加好友</span></p>
              </div>
            </div>
            <div class="flex gap-2">
              <button @click="handleFriendRequest(req.id, 'reject')" class="px-3 py-1 rounded-full text-xs font-medium text-gray-500 border border-gray-200 hover:bg-gray-50">拒绝</button>
              <button @click="handleFriendRequest(req.id, 'accept')" class="px-3 py-1 rounded-full text-xs font-medium text-white bg-indigo-600 hover:bg-indigo-700 shadow-sm">同意</button>
            </div>
          </div>
        </div>

        <div v-if="loadingFriends" class="text-center py-8 text-gray-500">加载中...</div>
        
        <div v-else-if="friends.length === 0" class="text-center py-8 text-gray-500">暂无好友</div>

        <div v-else class="grid grid-cols-2 gap-4">
          <div v-for="friend in friends" :key="friend.id" class="flex items-center justify-between p-3 rounded-xl border border-gray-50 hover:bg-gray-50 transition-colors group">
            <div class="flex items-center gap-3">
              <img :src="friend.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + friend.account" class="w-10 h-10 rounded-full" />
              <div>
                <p class="text-sm font-medium text-gray-800">{{ friend.username || friend.account }}</p>
                <span class="text-[10px] px-2 py-0.5 bg-gray-200 text-gray-600 rounded-md">{{ friend.groupName || '默认分组' }}</span>
              </div>
            </div>
            <div class="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
              <button @click="showGroupModal(friend)" class="p-1.5 text-gray-400 hover:text-indigo-600"><Settings class="w-4 h-4" /></button>
              <button @click="deleteFriend(friend.id)" class="p-1.5 text-gray-400 hover:text-red-500"><Trash2 class="w-4 h-4" /></button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showPasswordModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50" @click.self="showPasswordModal = false">
      <div class="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl">
        <h3 class="text-lg font-bold text-gray-800 mb-4">修改密码</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm text-gray-600 mb-1">原密码</label>
            <input v-model="passwordForm.oldPassword" type="password" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" placeholder="请输入原密码" />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">新密码</label>
            <input v-model="passwordForm.newPassword" type="password" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" placeholder="请输入新密码（6-20位）" />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">确认新密码</label>
            <input v-model="passwordForm.confirmPassword" type="password" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" placeholder="请再次输入新密码" />
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button @click="showPasswordModal = false" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg">取消</button>
          <button @click="changePassword" :disabled="changingPassword" class="px-4 py-2 text-sm bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 disabled:opacity-50">
            {{ changingPassword ? '修改中...' : '确认修改' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="showGroupDialog" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50" @click.self="showGroupDialog = false">
      <div class="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl">
        <h3 class="text-lg font-bold text-gray-800 mb-4">设置分组</h3>
        <div class="space-y-2">
          <div v-for="group in groupOptions" :key="group" @click="selectGroup(group)" 
               class="p-3 rounded-lg cursor-pointer transition-colors"
               :class="selectedGroup === group ? 'bg-indigo-50 border border-indigo-200' : 'hover:bg-gray-50 border border-transparent'">
            <span class="text-sm text-gray-700">{{ group }}</span>
          </div>
          <div class="flex items-center gap-2 mt-4">
            <input v-model="customGroup" type="text" placeholder="自定义分组名称" class="flex-1 border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" />
            <button @click="selectGroup(customGroup)" class="px-4 py-2 text-sm bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200">添加</button>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button @click="showGroupDialog = false" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg">取消</button>
          <button @click="updateFriendGroup" :disabled="updatingGroup" class="px-4 py-2 text-sm bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 disabled:opacity-50">
            {{ updatingGroup ? '保存中...' : '确认' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ArrowLeft, Camera, Edit2, Search, Settings, Trash2, LogOut } from 'lucide-vue-next'
import { userApi, friendApi } from '../api'

const userInfo = ref({})
const editForm = ref({ username: '', motto: '' })
const saving = ref(false)
const avatarInput = ref(null)

const showPasswordModal = ref(false)
const passwordForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const changingPassword = ref(false)

const friends = ref([])
const friendRequests = ref([])
const loadingFriends = ref(false)
const searchKeyword = ref('')
const searchResult = ref(null)

const showGroupDialog = ref(false)
const selectedFriend = ref(null)
const selectedGroup = ref('')
const customGroup = ref('')
const updatingGroup = ref(false)
const groupOptions = ['默认', '同学', '同事', '朋友', '家人']

const loadUserInfo = async () => {
  try {
    const res = await userApi.getInfo()
    if (res.code === 200 && res.data) {
      userInfo.value = res.data
      editForm.value.username = res.data.username || ''
      editForm.value.motto = res.data.motto || ''
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const triggerAvatarUpload = () => {
  avatarInput.value?.click()
}

const handleAvatarChange = async (event) => {
  const file = event.target.files?.[0]
  if (!file) return
  
  const formData = new FormData()
  formData.append('file', file)
  
  try {
    const res = await userApi.updateAvatar(formData)
    if (res.code === 200 && res.data) {
      userInfo.value.avatar = res.data
      alert('头像更新成功')
    } else {
      alert(res.msg || '头像更新失败')
    }
  } catch (error) {
    console.error('上传头像失败:', error)
    alert('上传头像失败')
  }
}

const saveProfile = async () => {
  saving.value = true
  try {
    if (editForm.value.username !== userInfo.value.username) {
      await userApi.updateUsername({ username: editForm.value.username })
      userInfo.value.username = editForm.value.username
    }
    if (editForm.value.motto !== userInfo.value.motto) {
      await userApi.updateMotto({ motto: editForm.value.motto })
      userInfo.value.motto = editForm.value.motto
    }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    alert('保存成功')
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败')
  } finally {
    saving.value = false
  }
}

const changePassword = async () => {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword || !passwordForm.value.confirmPassword) {
    alert('请填写完整信息')
    return
  }
  if (passwordForm.value.newPassword.length < 6 || passwordForm.value.newPassword.length > 20) {
    alert('密码长度需在6-20位之间')
    return
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('两次输入的新密码不一致')
    return
  }
  
  changingPassword.value = true
  try {
    const res = await userApi.updatePassword({
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword,
      confirmPassword: passwordForm.value.confirmPassword
    })
    if (res.code === 200) {
      alert('密码修改成功，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
    } else {
      alert(res.msg || '密码修改失败')
    }
  } catch (error) {
    console.error('修改密码失败:', error)
    alert('修改密码失败')
  } finally {
    changingPassword.value = false
  }
}

const loadFriends = async () => {
  loadingFriends.value = true
  try {
    const res = await friendApi.getList()
    if (res.code === 200 && res.data) {
      friends.value = res.data
    }
  } catch (error) {
    console.error('获取好友列表失败:', error)
  } finally {
    loadingFriends.value = false
  }
}

const loadFriendRequests = async () => {
  try {
    const res = await friendApi.getRequests()
    if (res.code === 200 && res.data) {
      friendRequests.value = res.data
    }
  } catch (error) {
    console.error('获取好友请求失败:', error)
  }
}

const searchUser = async () => {
  if (!searchKeyword.value.trim()) {
    alert('请输入搜索关键词')
    return
  }
  try {
    const res = await friendApi.searchUser(searchKeyword.value.trim())
    if (res.code === 200 && res.data && res.data.length > 0) {
      searchResult.value = res.data[0]
    } else {
      alert(res.msg || '未找到用户')
      searchResult.value = null
    }
  } catch (error) {
    console.error('搜索用户失败:', error)
    alert('搜索用户失败')
  }
}

const sendFriendRequest = async (userId) => {
  try {
    const res = await friendApi.sendRequest(userId)
    if (res.code === 200) {
      alert('好友请求已发送')
      searchResult.value = null
      searchKeyword.value = ''
    } else {
      alert(res.msg || '发送请求失败')
    }
  } catch (error) {
    console.error('发送好友请求失败:', error)
    alert('发送好友请求失败')
  }
}

const handleFriendRequest = async (requestId, action) => {
  try {
    const res = action === 'accept' 
      ? await friendApi.acceptRequest(requestId)
      : await friendApi.rejectRequest(requestId)
    if (res.code === 200) {
      alert(action === 'accept' ? '已同意好友请求' : '已拒绝好友请求')
      loadFriendRequests()
      loadFriends()
    } else {
      alert(res.msg || '操作失败')
    }
  } catch (error) {
    console.error('处理好友请求失败:', error)
    alert('处理好友请求失败')
  }
}

const deleteFriend = async (friendId) => {
  if (!confirm('确定要删除该好友吗？')) return
  try {
    const res = await friendApi.delete(friendId)
    if (res.code === 200) {
      alert('已删除好友')
      loadFriends()
    } else {
      alert(res.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除好友失败:', error)
    alert('删除好友失败')
  }
}

const showGroupModal = (friend) => {
  selectedFriend.value = friend
  selectedGroup.value = friend.groupName || '默认'
  customGroup.value = ''
  showGroupDialog.value = true
}

const selectGroup = (group) => {
  if (group) {
    selectedGroup.value = group
  }
}

const logout = () => {
  if (confirm('确定要退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    window.location.href = '/login'
  }
}

const updateFriendGroup = async () => {
  if (!selectedGroup.value) {
    alert('请选择或输入分组名称')
    return
  }
  updatingGroup.value = true
  try {
    const res = await friendApi.updateGroup(selectedFriend.value.id, selectedGroup.value)
    if (res.code === 200) {
      alert('分组已更新')
      showGroupDialog.value = false
      loadFriends()
    } else {
      alert(res.msg || '更新失败')
    }
  } catch (error) {
    console.error('更新分组失败:', error)
    alert('更新分组失败')
  } finally {
    updatingGroup.value = false
  }
}

onMounted(() => {
  const cached = localStorage.getItem('userInfo')
  if (cached) {
    userInfo.value = JSON.parse(cached)
    editForm.value.username = userInfo.value.username || ''
    editForm.value.motto = userInfo.value.motto || ''
  }
  loadUserInfo()
  loadFriends()
  loadFriendRequests()
})
</script>
