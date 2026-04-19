<template>
  <div class="min-h-screen bg-[#f8fafc] p-6">
    <div class="max-w-6xl mx-auto space-y-6">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <button @click="$router.back()" class="p-2 text-gray-400 hover:bg-gray-200 rounded-full transition-all">
            <ArrowLeft class="w-5 h-5" />
          </button>
          <h1 class="text-2xl font-bold text-gray-800">分享给我的笔记</h1>
        </div>
      </div>

      <div class="flex gap-4 border-b border-gray-200">
        <button @click="activeTab = 'shared'" 
                class="pb-3 px-4 text-sm font-medium transition-colors"
                :class="activeTab === 'shared' ? 'text-indigo-600 border-b-2 border-indigo-600' : 'text-gray-500 hover:text-gray-700'">
          好友分享
        </button>
        <button @click="activeTab = 'public'" 
                class="pb-3 px-4 text-sm font-medium transition-colors"
                :class="activeTab === 'public' ? 'text-indigo-600 border-b-2 border-indigo-600' : 'text-gray-500 hover:text-gray-700'">
          公开笔记
        </button>
      </div>

      <div v-if="loading" class="text-center py-12 text-gray-500">
        <div class="animate-spin w-8 h-8 border-2 border-indigo-500 border-t-transparent rounded-full mx-auto mb-4"></div>
        加载中...
      </div>

      <div v-else-if="notes.length === 0" class="text-center py-12 text-gray-500">
        <FileText class="w-12 h-12 mx-auto mb-4 text-gray-300" />
        <p>{{ activeTab === 'shared' ? '暂无好友分享的笔记' : '暂无公开笔记' }}</p>
      </div>

      <div v-else class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <div v-for="note in notes" :key="note.id" 
             @click="viewNote(note)"
             class="bg-white rounded-2xl p-5 shadow-sm border border-gray-100 cursor-pointer hover:shadow-md hover:border-indigo-100 transition-all">
          <div class="flex items-start justify-between mb-3">
            <h3 class="font-bold text-gray-800 line-clamp-1">{{ note.title }}</h3>
            <span class="rounded-full bg-green-50 px-2 py-0.5 text-xs font-medium text-green-700">
              {{ note.visibility === 'PUBLIC' ? '公开' : '好友分享' }}
            </span>
          </div>
          <p class="text-sm text-gray-600 line-clamp-3 mb-4">{{ note.content }}</p>
          <div class="flex items-center justify-between text-xs text-gray-500">
            <div class="flex items-center gap-2">
              <img :src="note.authorAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + note.authorAccount" 
                   class="w-5 h-5 rounded-full" />
              <span>{{ note.authorName || note.authorAccount || '未知用户' }}</span>
            </div>
            <span>{{ note.updateTime?.split('T')[0] || '' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, FileText } from 'lucide-vue-next'
import { noteApi } from '../api'

const router = useRouter()
const activeTab = ref('shared')
const notes = ref([])
const loading = ref(false)

const fetchSharedNotes = async () => {
  loading.value = true
  try {
    const res = await noteApi.getSharedNotes(activeTab.value)
    if (res.code === 200 && res.data) {
      notes.value = res.data
    }
  } catch (error) {
    console.error('获取分享笔记失败:', error)
  } finally {
    loading.value = false
  }
}

const viewNote = (note) => {
  router.push(`/shared/${note.id}`)
}

watch(activeTab, () => {
  fetchSharedNotes()
})

onMounted(() => {
  fetchSharedNotes()
})
</script>

<style scoped>
.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
