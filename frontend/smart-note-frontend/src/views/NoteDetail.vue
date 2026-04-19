<template>
  <div class="min-h-screen bg-[#fafafa] p-6">
    <div class="max-w-6xl mx-auto flex flex-col gap-6 lg:flex-row items-start">
      <div class="flex-1 bg-white rounded-[1rem] p-8 shadow-sm border border-gray-100 min-h-[85vh]">
        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center gap-4">
            <button @click="router.back()" class="p-2 text-gray-400 hover:bg-gray-100 rounded-full transition-all"><ArrowLeft class="w-5 h-5" /></button>
            <div>
              <h1 class="text-2xl font-bold text-gray-900">{{ note?.title || '笔记详情' }}</h1>
              <p class="text-sm text-gray-500">ID: {{ note?.id || route.params.id }}</p>
            </div>
          </div>
          <div class="flex items-center gap-2">
            <span class="rounded-full bg-indigo-50 px-3 py-1 text-xs font-semibold text-indigo-700">{{ getVisibilityText(note?.visibility) }}</span>
            <button @click="toggleEditMode" v-if="!isEditing" class="p-2 text-gray-400 hover:bg-gray-100 rounded-full transition-all">
              <Edit3 class="w-5 h-5" />
            </button>
            <button @click="confirmDelete" class="p-2 text-gray-400 hover:bg-red-50 hover:text-red-500 rounded-full transition-all">
              <Trash2 class="w-5 h-5" />
            </button>
            <button @click="openShareModal" class="p-2 text-gray-400 hover:bg-gray-100 rounded-full transition-all">
              <Share2 class="w-5 h-5" />
            </button>
          </div>
        </div>

        <div v-if="loading" class="text-center py-12 text-gray-500">
          <div class="animate-spin w-8 h-8 border-2 border-indigo-500 border-t-transparent rounded-full mx-auto mb-4"></div>
          加载中...
        </div>

        <div v-else-if="!note" class="text-center py-12 text-gray-500">
          <FileText class="w-12 h-12 mx-auto mb-4 text-gray-300" />
          <p>笔记不存在或无权访问</p>
          <button @click="router.push('/notes')" class="mt-4 text-indigo-600 hover:underline">返回笔记列表</button>
        </div>

        <template v-else>
          <div v-if="!isEditing" class="space-y-8">
            <section class="prose max-w-none text-gray-700 leading-relaxed">
              <p v-if="note?.content" class="whitespace-pre-wrap">{{ note.content }}</p>
              <p v-else class="text-gray-500">这条笔记暂时没有内容。</p>
            </section>

            <div class="grid gap-4 sm:grid-cols-2">
              <div class="rounded-[1rem] border border-gray-200 bg-gradient-to-br from-indigo-50 to-purple-50 p-5">
                <h3 class="text-sm font-semibold text-gray-800 mb-3 flex items-center gap-2">
                  <FileText class="w-4 h-4 text-indigo-500" /> AI 摘要
                </h3>
                <p class="text-sm text-gray-600">{{ note?.summary || '暂无摘要，点击右侧"开始分析"生成' }}</p>
              </div>
              <div class="rounded-[1rem] border border-gray-200 bg-gradient-to-br from-green-50 to-teal-50 p-5">
                <h3 class="text-sm font-semibold text-gray-800 mb-3 flex items-center gap-2">
                  <ListChecks class="w-4 h-4 text-green-500" /> 关键要点
                </h3>
                <p class="text-sm text-gray-600 whitespace-pre-wrap">{{ note?.keyPoints || '暂无要点，点击右侧"开始分析"生成' }}</p>
              </div>
            </div>

            <div class="grid gap-4 sm:grid-cols-2">
              <div class="rounded-[1rem] border border-gray-200 bg-gradient-to-br from-orange-50 to-amber-50 p-5">
                <h3 class="text-sm font-semibold text-gray-800 mb-3 flex items-center gap-2">
                  <Tags class="w-4 h-4 text-orange-500" /> AI 标签建议
                </h3>
                <div class="flex flex-wrap gap-2">
                  <span v-if="note?.tagsSuggestion" v-for="tag in note.tagsSuggestion.split(',')" :key="tag" class="px-2 py-1 bg-orange-100 text-orange-700 rounded-full text-xs">{{ tag.trim() }}</span>
                  <span v-else class="text-sm text-gray-500">暂无标签建议</span>
                </div>
              </div>
              <div class="rounded-[1rem] border border-gray-200 bg-gradient-to-br from-blue-50 to-cyan-50 p-5">
                <h3 class="text-sm font-semibold text-gray-800 mb-3 flex items-center gap-2">
                  <Tag class="w-4 h-4 text-blue-500" /> 当前标签
                </h3>
                <div class="flex flex-wrap gap-2">
                  <span v-if="note?.tags" v-for="tag in note.tags.split(',')" :key="tag" class="px-2 py-1 bg-blue-100 text-blue-700 rounded-full text-xs">{{ tag.trim() }}</span>
                  <span v-else class="text-sm text-gray-500">未设置标签</span>
                </div>
              </div>
            </div>
          </div>

          <div v-else class="space-y-4">
            <div>
              <label class="block text-sm text-gray-600 mb-1">标题</label>
              <input v-model="editForm.title" type="text" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" placeholder="请输入笔记标题" />
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">内容</label>
              <textarea v-model="editForm.content" rows="12" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none resize-none" placeholder="请输入笔记内容"></textarea>
            </div>
            <div>
              <label class="block text-sm text-gray-600 mb-1">标签（多个标签用逗号分隔）</label>
              <input v-model="editForm.tags" type="text" class="w-full border border-gray-200 rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none" placeholder="例如：学习,技术,笔记" />
            </div>
            <div class="flex justify-end gap-3 pt-4">
              <button @click="cancelEdit" class="px-4 py-2 text-sm text-gray-600 hover:bg-gray-100 rounded-lg">取消</button>
              <button @click="saveEdit" :disabled="saving" class="px-4 py-2 text-sm bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 disabled:opacity-50">
                {{ saving ? '保存中...' : '保存' }}
              </button>
            </div>
          </div>

          <div v-if="!isEditing" class="mt-10 pt-6 border-t border-gray-100 flex flex-col gap-3 text-sm text-gray-500">
            <div>标签：{{ note?.tags || '无' }}</div>
            <div>访问级别：{{ getVisibilityText(note?.visibility) }}</div>
            <div>最后修改：{{ note?.updateTime ? note.updateTime.split('T')[0] : '未知' }}</div>
          </div>
        </template>
      </div>

      <aside class="w-full lg:w-80 sticky top-6 space-y-4">
        <div class="bg-indigo-600 rounded-[1rem] p-6 text-white shadow-lg shadow-indigo-200/50 relative overflow-hidden">
          <div class="absolute right-0 top-0 w-32 h-32 bg-white/10 rounded-full blur-2xl transform translate-x-1/2 -translate-y-1/2"></div>
          <h3 class="font-bold mb-2 flex items-center gap-2"><Sparkles class="w-4 h-4"/> AI 智能分析</h3>
          <p class="text-xs text-indigo-100 mb-4 opacity-90">一键提取核心要点与摘要</p>
          <button @click="analyzeNote" :disabled="analyzing" class="w-full py-2 bg-white text-indigo-600 rounded-full text-sm font-bold shadow-sm hover:scale-105 active:scale-95 transition-all disabled:opacity-50">
            {{ analyzing ? '分析中...' : (note?.summary ? '重新分析' : '开始分析') }}
          </button>
        </div>

        <div v-if="note?.visibility === 'PUBLIC'" class="bg-white rounded-[1rem] p-5 shadow-sm border border-gray-100">
          <h4 class="text-sm font-bold text-gray-800 mb-3 flex items-center gap-2">
            <div class="w-1 h-3 bg-green-500 rounded-full"></div>分享链接
          </h4>
          <div class="flex gap-2">
            <input :value="shareUrl" readonly class="flex-1 bg-gray-50 border border-gray-200 rounded-lg px-3 py-2 text-xs" />
            <button @click="copyShareUrl" class="px-3 py-2 bg-indigo-50 text-indigo-600 rounded-lg text-xs hover:bg-indigo-100">复制</button>
          </div>
        </div>

        <div class="bg-white rounded-[1rem] p-5 shadow-sm border border-gray-100">
          <h4 class="text-sm font-bold text-gray-800 mb-3 flex items-center gap-2">
            <div class="w-1 h-3 bg-indigo-500 rounded-full"></div>操作提示
          </h4>
          <p class="text-xs text-gray-500 leading-relaxed bg-gray-50 p-3 rounded-lg">
            点击编辑按钮可修改笔记内容。AI分析结果会自动保存并与笔记关联。设置可见性后可分享给好友或公开访问。
          </p>
        </div>
      </aside>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Sparkles, Edit3, Trash2, Share2, FileText, ListChecks, Tags, Tag } from 'lucide-vue-next'
import { noteApi, friendApi } from '../api'

const route = useRoute()
const router = useRouter()
const note = ref(null)
const loading = ref(false)
const analyzing = ref(false)

const isEditing = ref(false)
const editForm = ref({ title: '', content: '', tags: '' })
const saving = ref(false)

const showShareModal = ref(false)
const shareForm = ref({ visibility: 'PRIVATE', friendIds: [] })
const sharing = ref(false)
const friends = ref([])

const visibilityOptions = [
  { value: 0, label: '仅自己可见', desc: '默认状态，只有自己可以查看和编辑' },
  { value: 1, label: '部分好友可见', desc: '选中的好友可以查看，但不能编辑' },
  { value: 2, label: '部分好友可编辑', desc: '选中的好友可以查看和编辑' },
  { value: 3, label: '所有人可见', desc: '所有人（包括非好友）可以查看，但不能编辑' }
]

const shareUrl = computed(() => {
  if (!note.value) return ''
  return `${window.location.origin}/note/${note.value.id}`
})

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

const fetchNote = async () => {
  loading.value = true
  try {
    const res = await noteApi.getDetail(route.params.id)
    if (res.code === 200 && res.data) {
      note.value = res.data
    }
  } catch (error) {
    console.error('加载笔记详情失败:', error)
  } finally {
    loading.value = false
  }
}

const toggleEditMode = () => {
  isEditing.value = true
  editForm.value = {
    title: note.value?.title || '',
    content: note.value?.content || '',
    tags: note.value?.tags || ''
  }
}

const cancelEdit = () => {
  isEditing.value = false
}

const saveEdit = async () => {
  if (!editForm.value.title.trim()) {
    alert('请输入笔记标题')
    return
  }
  
  saving.value = true
  try {
    const res = await noteApi.update(note.value.id, {
      title: editForm.value.title,
      content: editForm.value.content,
      tags: editForm.value.tags
    })
    if (res.code === 200) {
      note.value.title = editForm.value.title
      note.value.content = editForm.value.content
      note.value.tags = editForm.value.tags
      isEditing.value = false
      alert('笔记更新成功')
    } else {
      alert(res.msg || '更新失败')
    }
  } catch (error) {
    console.error('更新笔记失败:', error)
    alert('更新笔记失败')
  } finally {
    saving.value = false
  }
}

const confirmDelete = async () => {
  if (!confirm('确定要删除这篇笔记吗？')) return
  
  try {
    const res = await noteApi.delete(note.value.id)
    if (res.code === 200) {
      alert('笔记已删除')
      router.push('/notes')
    } else {
      alert(res.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除笔记失败:', error)
    alert('删除笔记失败')
  }
}

const analyzeNote = async () => {
  analyzing.value = true
  try {
    const res = await noteApi.analyze(note.value.id)
    if (res.code === 200 && res.data) {
      note.value.summary = res.data.summary
      note.value.keyPoints = res.data.keyPoints
      note.value.tagsSuggestion = res.data.tags
      alert('AI分析完成')
    } else {
      alert(res.msg || '分析失败')
    }
  } catch (error) {
    console.error('AI分析失败:', error)
    alert('AI分析失败，请检查后端服务')
  } finally {
    analyzing.value = false
  }
}

const openShareModal = async () => {
  const visibilityMap = { 'PRIVATE': 0, 'FRIEND_READ': 1, 'FRIEND_WRITE': 2, 'PUBLIC': 3 }
  const visibilityValue = typeof note.value?.visibility === 'string' 
    ? (visibilityMap[note.value.visibility] ?? 0) 
    : (note.value?.visibility ?? 0)
  shareForm.value = {
    visibility: visibilityValue,
    friendIds: note.value?.sharedFriendIds || []
  }
  
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
  sharing.value = true
  try {
    const res = await noteApi.setVisibility(note.value.id, shareForm.value.visibility)
    if (res.code === 200) {
      note.value.visibility = shareForm.value.visibility
      showShareModal.value = false
      alert('分享设置已保存')
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

const copyShareUrl = () => {
  navigator.clipboard.writeText(shareUrl.value)
  alert('链接已复制到剪贴板')
}

onMounted(fetchNote)
</script>
