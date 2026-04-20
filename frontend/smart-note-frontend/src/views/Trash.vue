<template>
  <div class="min-h-screen bg-[#f8fafc] p-6">
    <div class="max-w-6xl mx-auto space-y-6">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <button @click="$router.back()" class="p-2 text-gray-400 hover:bg-gray-200 rounded-full transition-all">
            <ArrowLeft class="w-5 h-5" />
          </button>
          <h1 class="text-2xl font-bold text-gray-800">回收站</h1>
        </div>
        <button v-if="notes.length > 0" @click="emptyTrash" class="text-sm text-red-600 hover:text-red-700">
          清空回收站
        </button>
      </div>

      <div v-if="loading" class="text-center py-12 text-gray-500">
        <div class="animate-spin w-8 h-8 border-2 border-indigo-500 border-t-transparent rounded-full mx-auto mb-4"></div>
        加载中...
      </div>

      <div v-else-if="notes.length === 0" class="text-center py-12 text-gray-500">
        <Trash2 class="w-12 h-12 mx-auto mb-4 text-gray-300" />
        <p>回收站是空的</p>
      </div>

      <div v-else class="space-y-4">
        <div v-for="note in notes" :key="note.id" 
             class="bg-white rounded-2xl p-5 shadow-sm border border-gray-100 hover:border-gray-200 transition-all">
          <div class="flex items-start justify-between">
            <div class="flex-1 min-w-0">
              <h3 class="font-bold text-gray-800 mb-2">{{ note.title }}</h3>
              <p class="text-sm text-gray-500 line-clamp-2 mb-3">{{ note.content }}</p>
              <div class="flex items-center gap-4 text-xs text-gray-400">
                <span>删除于 {{ note.updateTime?.split('T')[0] }}</span>
                <span v-if="note.tags">{{ note.tags }}</span>
              </div>
            </div>
            <div class="flex gap-2 ml-4">
              <button @click="restoreNote(note.id)" 
                      class="px-3 py-1.5 text-xs font-medium text-indigo-600 bg-indigo-50 rounded-full hover:bg-indigo-100 transition-colors">
                恢复
              </button>
              <button @click="deletePermanently(note.id)" 
                      class="px-3 py-1.5 text-xs font-medium text-red-600 bg-red-50 rounded-full hover:bg-red-100 transition-colors">
                彻底删除
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, Trash2 } from 'lucide-vue-next'
import { noteApi } from '../api'

const router = useRouter()
const notes = ref([])
const loading = ref(false)

const fetchTrashNotes = async () => {
  loading.value = true
  try {
    const res = await noteApi.getTrash()
    if (res.code === 200 && res.data) {
      notes.value = res.data
    }
  } catch (error) {
    console.error('获取回收站失败:', error)
  } finally {
    loading.value = false
  }
}

const restoreNote = async (id) => {
  if (!confirm('确定要恢复这篇笔记吗？')) return
  try {
    const res = await noteApi.restore(id)
    if (res.code === 200) {
      alert('笔记已恢复')
      fetchTrashNotes()
    } else {
      alert(res.msg || '恢复失败')
    }
  } catch (error) {
    console.error('恢复笔记失败:', error)
    alert('恢复失败')
  }
}

const deletePermanently = async (id) => {
  if (!confirm('确定要彻底删除这篇笔记吗？此操作不可撤销！')) return
  try {
    const res = await noteApi.delete(id)
    if (res.code === 200) {
      alert('笔记已彻底删除')
      fetchTrashNotes()
    } else {
      alert(res.msg || '删除失败')
    }
  } catch (error) {
    console.error('彻底删除失败:', error)
    alert('删除失败')
  }
}

const emptyTrash = async () => {
  if (!confirm('确定要清空回收站吗？此操作不可撤销！')) return
  try {
    const res = await noteApi.emptyTrash()
    if (res.code === 200) {
      alert('回收站已清空')
      notes.value = []
    } else {
      alert(res.msg || '操作失败')
    }
  } catch (error) {
    console.error('清空回收站失败:', error)
    alert('操作失败')
  }
}

onMounted(() => {
  fetchTrashNotes()
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
