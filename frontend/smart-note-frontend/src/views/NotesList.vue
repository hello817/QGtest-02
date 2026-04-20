<template>
  <div class="min-h-screen bg-[#f8fafc] p-6">
    <div class="max-w-6xl mx-auto space-y-6">
      <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">我的笔记</h1>
          <p class="text-sm text-gray-500 mt-2">管理你的所有笔记，支持搜索、标签筛选和分页</p>
        </div>
        <div class="flex gap-3">
          <button @click="router.push('/shared')" class="inline-flex items-center justify-center rounded-full bg-green-50 px-5 py-3 text-green-700 transition hover:bg-green-100">
            <Share2 class="w-4 h-4 mr-2" /> 分享给我
          </button>
          <button @click="router.push('/trash')" class="inline-flex items-center justify-center rounded-full bg-gray-100 px-5 py-3 text-gray-700 transition hover:bg-gray-200">
            <Trash2 class="w-4 h-4 mr-2" /> 回收站
          </button>
          <button @click="showHistoryModal = true" class="inline-flex items-center justify-center rounded-full bg-gray-100 px-5 py-3 text-gray-700 transition hover:bg-gray-200">
            <History class="w-4 h-4 mr-2" /> 历史记录
          </button>
          <button @click="router.push('/profile')" class="inline-flex items-center justify-center rounded-full bg-gray-100 px-5 py-3 text-gray-700 transition hover:bg-gray-200">
            <User class="w-4 h-4 mr-2" /> 个人中心
          </button>
          <button @click="openCreateModal" class="inline-flex items-center justify-center rounded-full bg-indigo-600 px-5 py-3 text-white transition hover:bg-indigo-700">
            <Plus class="w-4 h-4 mr-2" /> 新建笔记
          </button>
        </div>
      </div>

      <div class="bg-white rounded-2xl p-4 shadow-sm border border-gray-100">
        <div class="flex flex-wrap gap-4 items-center">
          <div class="relative flex-1 min-w-[200px]">
            <Search class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input v-model="searchForm.keyword" @keyup.enter="searchNotes" type="text" placeholder="搜索标题或内容..." class="w-full bg-gray-50 border-none rounded-full py-2 pl-10 pr-4 text-sm focus:ring-2 focus:ring-indigo-500/20 outline-none" />
          </div>
          <div class="flex items-center gap-2">
            <Tag class="w-4 h-4 text-gray-400" />
            <input v-model="searchForm.tag" @keyup.enter="searchNotes" type="text" placeholder="标签筛选" class="bg-gray-50 border-none rounded-full py-2 px-4 text-sm w-32 focus:ring-2 focus:ring-indigo-500/20 outline-none" />
          </div>
          <button @click="searchNotes" class="bg-indigo-50 text-indigo-600 px-4 py-2 rounded-full text-sm font-medium hover:bg-indigo-100 transition-colors">搜索</button>
          <button @click="resetSearch" class="text-gray-500 px-4 py-2 rounded-full text-sm hover:bg-gray-100 transition-colors">重置</button>
        </div>
      </div>

      <div v-if="loading" class="rounded-[1.5rem] border border-gray-200 bg-white p-8 text-center text-gray-500">
        <div class="animate-spin w-8 h-8 border-2 border-indigo-500 border-t-transparent rounded-full mx-auto mb-4"></div>
        正在加载笔记...
      </div>

      <div v-if="!loading && notes.length === 0" class="rounded-[1.5rem] border border-dashed border-gray-300 bg-white p-10 text-center text-gray-500">
        <FileText class="w-12 h-12 mx-auto mb-4 text-gray-300" />
        <p>暂无笔记</p>
        <button @click="openCreateModal" class="mt-4 text-indigo-600 hover:underline">创建第一篇笔记</button>
      </div>

      <div v-if="!loading && notes.length > 0" class="grid gap-5 md:grid-cols-2">
        <article v-for="note in notes" :key="note.id" class="cursor-pointer overflow-hidden rounded-[1.5rem] border border-gray-200 bg-white p-6 shadow-sm transition hover:-translate-y-1 hover:shadow-lg group">
          <div class="flex items-start justify-between gap-4">
            <div @click="openNote(note.id)" class="flex-1">
              <h2 class="text-xl font-semibold text-gray-900 group-hover:text-indigo-600 transition-colors">{{ note.title || '无标题笔记' }}</h2>
              <p class="mt-2 text-sm text-gray-500">{{ note.tags || '未设置标签' }}</p>
            </div>
            <div class="flex items-center gap-2">
              <span class="rounded-full bg-indigo-50 px-3 py-1 text-xs font-semibold text-indigo-700">{{ getVisibilityText(note.visibility) }}</span>
              <div class="relative" @click.stop>
                <button @click="toggleNoteMenu(note.id)" class="p-1 text-gray-400 hover:text-gray-600">
                  <MoreVertical class="w-4 h-4" />
                </button>
                <div v-if="activeMenu === note.id" class="absolute right-0 top-6 bg-white rounded-lg shadow-lg border border-gray-100 py-1 z-10 min-w-[100px]">
                  <button @click="openEditModal(note)" class="w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-gray-50 flex items-center gap-2">
                    <Edit3 class="w-3 h-3" /> 编辑
                  </button>
                  <button @click="openShareModal(note)" class="w-full px-4 py-2 text-left text-sm text-gray-700 hover:bg-gray-50 flex items-center gap-2">
                    <Share2 class="w-3 h-3" /> 分享
                  </button>
                  <button @click="deleteNote(note.id)" class="w-full px-4 py-2 text-left text-sm text-red-600 hover:bg-red-50 flex items-center gap-2">
                    <Trash2 class="w-3 h-3" /> 删除
                  </button>
                </div>
              </div>
            </div>
          </div>
          <p @click="openNote(note.id)" class="mt-5 text-sm leading-7 text-gray-600 line-clamp-3">{{ note.content || '这条笔记暂时没有内容。' }}</p>
          <div @click="openNote(note.id)" class="mt-6 flex items-center justify-between text-xs text-gray-400">
            <span>{{ note.updateTime ? note.updateTime.split('T')[0] : '更新时间未知' }}</span>
            <span v-if="note.summary" class="text-indigo-500">AI已分析</span>
          </div>
        </article>
      </div>

      <div v-if="totalPages > 1" class="flex justify-center items-center gap-2 mt-8">
        <button @click="changePage(pageNum - 1)" :disabled="pageNum <= 1" class="px-4 py-2 rounded-lg text-sm bg-white border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed">
          上一页
        </button>
        <span class="px-4 py-2 text-sm text-gray-600">第 {{ pageNum }} / {{ totalPages }} 页</span>
        <button @click="changePage(pageNum + 1)" :disabled="pageNum >= totalPages" class="px-4 py-2 rounded-lg text-sm bg-white border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed">
          下一页
        </button>
      </div>
    </div>

    <div v-if="showCreateModal || showEditModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4" @click.self="closeModal">
      <div class="bg-white rounded-2xl p-6 w-full max-w-2xl shadow-xl max-h-[90vh] overflow-y-auto">
        <h3 class="text-lg font-bold text-gray-800 mb-4">{{ showEditModal ? '编辑笔记' : '新建笔记' }}</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm text-gray-600 mb-1">标题</label>
            <input v-model="noteForm.title" type="text" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" placeholder="请输入笔记标题" />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">内容</label>
            <textarea v-model="noteForm.content" rows="8" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none resize-none" placeholder="请输入笔记内容"></textarea>
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">标签（多个标签用逗号分隔）</label>
            <input v-model="noteForm.tags" type="text" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" placeholder="例如：学习,技术,笔记" />
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button @click="closeModal" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg">取消</button>
          <button @click="saveNote" :disabled="saving" class="px-4 py-2 text-sm bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 disabled:opacity-50">
            {{ saving ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="showShareModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4" @click.self="showShareModal = false">
      <div class="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl">
        <h3 class="text-lg font-bold text-gray-800 mb-4">分享笔记</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm text-gray-600 mb-2">可见性设置</label>
            <div class="space-y-2">
              <label v-for="option in visibilityOptions" :key="option.value" class="flex items-start gap-3 p-3 rounded-lg cursor-pointer transition-colors" :class="shareForm.visibility === option.value ? 'bg-indigo-50 border border-indigo-200' : 'hover:bg-gray-50 border border-transparent'">
                <input type="radio" :value="option.value" v-model="shareForm.visibility" class="mt-1" />
                <div>
                  <span class="text-sm font-medium text-gray-700">{{ option.label }}</span>
                  <p class="text-xs text-gray-500">{{ option.desc }}</p>
                </div>
              </label>
            </div>
          </div>
          <div v-if="shareForm.visibility === 'PARTIAL_VIEW' || shareForm.visibility === 'PARTIAL_EDIT'">
            <label class="block text-sm text-gray-600 mb-2">选择好友</label>
            <div class="max-h-40 overflow-y-auto border border-gray-200 rounded-lg p-2">
              <label v-for="friend in friends" :key="friend.id" class="flex items-center gap-2 p-2 hover:bg-gray-50 rounded cursor-pointer">
                <input type="checkbox" :value="friend.id" v-model="shareForm.friendIds" />
                <img :src="friend.avatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + friend.account" class="w-6 h-6 rounded-full" />
                <span class="text-sm">{{ friend.nickname || friend.account }}</span>
              </label>
            </div>
          </div>
        </div>
        <div class="flex justify-end gap-3 mt-6">
          <button @click="showShareModal = false" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg">取消</button>
          <button @click="saveShare" :disabled="sharing" class="px-4 py-2 text-sm bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 disabled:opacity-50">
            {{ sharing ? '保存中...' : '保存设置' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="showHistoryModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4" @click.self="showHistoryModal = false">
      <div class="bg-white rounded-2xl p-6 w-full max-w-2xl shadow-xl max-h-[80vh] overflow-y-auto">
        <h3 class="text-lg font-bold text-gray-800 mb-4">浏览历史</h3>
        <div v-if="loadingHistory" class="text-center py-8 text-gray-500">加载中...</div>
        <div v-else-if="historyList.length === 0" class="text-center py-8 text-gray-500">暂无浏览记录</div>
        <div v-else class="space-y-3">
          <div v-for="item in historyList" :key="item.id" @click="openNoteFromHistory(item)" class="flex items-center gap-4 p-3 rounded-xl hover:bg-gray-50 cursor-pointer transition-colors">
            <FileText class="w-8 h-8 text-gray-400" />
            <div class="flex-1">
              <p class="text-sm font-medium text-gray-800">{{ item.title || '无标题笔记' }}</p>
              <p class="text-xs text-gray-500">{{ item.viewTime || item.updateTime }}</p>
            </div>
          </div>
        </div>
        <div class="flex justify-end mt-6">
          <button @click="showHistoryModal = false" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus, User, Search, Tag, FileText, Edit3, Trash2, Share2, MoreVertical, History, LogOut } from 'lucide-vue-next'
import { noteApi, friendApi } from '../api'

const router = useRouter()
const notes = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const totalPages = ref(0)
const activeMenu = ref(null)

const searchForm = ref({ keyword: '', tag: '' })

const showCreateModal = ref(false)
const showEditModal = ref(false)
const editingNote = ref(null)
const noteForm = ref({ title: '', content: '', tags: '' })
const saving = ref(false)

const showShareModal = ref(false)
const sharingNote = ref(null)
const shareForm = ref({ visibility: 'PRIVATE', friendIds: [] })
const sharing = ref(false)
const friends = ref([])

const showHistoryModal = ref(false)
const historyList = ref([])
const loadingHistory = ref(false)

const visibilityOptions = [
  { value: 0, label: '仅自己可见', desc: '默认状态，只有自己可以查看和编辑' },
  { value: 1, label: '部分好友可见', desc: '选中的好友可以查看，但不能编辑' },
  { value: 2, label: '部分好友可编辑', desc: '选中的好友可以查看和编辑' },
  { value: 3, label: '所有人可见', desc: '所有人（包括非好友）可以查看，但不能编辑' }
]

const getVisibilityText = (visibility) => {
  const map = { 
    'PRIVATE': '私有', 
    'FRIEND_READ': '部分可见', 
    'FRIEND_WRITE': '部分可编辑', 
    'PUBLIC': '公开',
    0: '私有', 
    1: '部分可见', 
    2: '部分可编辑', 
    3: '公开'
  }
  return map[visibility] ?? '私有'
}

const fetchNotes = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (searchForm.value.keyword) params.keyword = searchForm.value.keyword
    if (searchForm.value.tag) params.tag = searchForm.value.tag
    
    const res = await noteApi.getList(params)
    if (res.code === 200 && res.data) {
      notes.value = res.data.data || []
      total.value = res.data.total || 0
      totalPages.value = res.data.pages || Math.ceil(total.value / pageSize.value)
    }
  } catch (error) {
    console.error('获取笔记列表失败:', error)
  } finally {
    loading.value = false
  }
}

const searchNotes = () => {
  pageNum.value = 1
  fetchNotes()
}

const resetSearch = () => {
  searchForm.value = { keyword: '', tag: '' }
  pageNum.value = 1
  fetchNotes()
}

const changePage = (page) => {
  if (page < 1 || page > totalPages.value) return
  pageNum.value = page
  fetchNotes()
}

const openNote = (id) => {
  router.push(`/note/${id}`)
}

const toggleNoteMenu = (id) => {
  activeMenu.value = activeMenu.value === id ? null : id
}

const openCreateModal = () => {
  noteForm.value = { title: '', content: '', tags: '' }
  showCreateModal.value = true
  showEditModal.value = false
}

const openEditModal = (note) => {
  editingNote.value = note
  noteForm.value = {
    title: note.title || '',
    content: note.content || '',
    tags: note.tags || ''
  }
  showEditModal.value = true
  showCreateModal.value = false
  activeMenu.value = null
}

const closeModal = () => {
  showCreateModal.value = false
  showEditModal.value = false
  editingNote.value = null
}

const saveNote = async () => {
  if (!noteForm.value.title.trim()) {
    alert('请输入笔记标题')
    return
  }
  
  saving.value = true
  try {
    const data = {
      title: noteForm.value.title,
      content: noteForm.value.content,
      tags: noteForm.value.tags
    }
    
    if (showEditModal.value && editingNote.value) {
      const res = await noteApi.update(editingNote.value.id, data)
      if (res.code === 200) {
        alert('笔记更新成功')
        closeModal()
        fetchNotes()
      } else {
        alert(res.msg || '更新失败')
      }
    } else {
      const res = await noteApi.create(data)
      if (res.code === 200) {
        alert('笔记创建成功')
        closeModal()
        fetchNotes()
      } else {
        alert(res.msg || '创建失败')
      }
    }
  } catch (error) {
    console.error('保存笔记失败:', error)
    alert('保存笔记失败')
  } finally {
    saving.value = false
  }
}

const deleteNote = async (id) => {
  if (!confirm('确定要删除这篇笔记吗？删除后可在回收站恢复')) return
  
  try {
    const res = await noteApi.trash(id)
    if (res.code === 200) {
      alert('笔记已移入回收站')
      fetchNotes()
    } else {
      alert(res.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除笔记失败:', error)
    alert('删除笔记失败')
  } finally {
    activeMenu.value = null
  }
}

const openShareModal = async (note) => {
  sharingNote.value = note
  const visibilityMap = { 'PRIVATE': 0, 'FRIEND_READ': 1, 'FRIEND_WRITE': 2, 'PUBLIC': 3 }
  const visibilityValue = typeof note.visibility === 'string' 
    ? (visibilityMap[note.visibility] ?? 0) 
    : (note.visibility ?? 0)
  shareForm.value = {
    visibility: visibilityValue,
    friendIds: note.sharedFriendIds || []
  }
  activeMenu.value = null
  
  try {
    const res = await friendApi.getList()
    if (res.code === 200 && res.data) {
      friends.value = res.data
    }
  } catch (error) {
    console.error('获取好友列表失败:', error)
  }
  
  showShareModal.value = true
}

const saveShare = async () => {
  if (!sharingNote.value) return
  
  sharing.value = true
  try {
    const res = await noteApi.setVisibility(sharingNote.value.id, shareForm.value.visibility)
    if (res.code === 200) {
      alert('分享设置已保存')
      showShareModal.value = false
      fetchNotes()
    } else {
      alert(res.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存分享设置失败:', error)
    alert('保存分享设置失败')
  } finally {
    sharing.value = false
  }
}

const loadHistory = async () => {
  loadingHistory.value = true
  try {
    const res = await noteApi.getHistory()
    if (res.code === 200 && res.data) {
      historyList.value = res.data
    }
  } catch (error) {
    console.error('获取历史记录失败:', error)
  } finally {
    loadingHistory.value = false
  }
}

const openNoteFromHistory = (item) => {
  showHistoryModal.value = false
  router.push(`/note/${item.id}`)
}

const handleClickOutside = (event) => {
  if (!event.target.closest('.relative')) {
    activeMenu.value = null
  }
}

const logout = () => {
  if (confirm('确定要退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    window.location.href = '/login'
  }
}

onMounted(() => {
  fetchNotes()
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
