<template>
  <div class="min-h-screen bg-[#fafafa] p-6">
    <div class="max-w-4xl mx-auto">
      <div class="flex items-center gap-4 mb-6">
        <button @click="$router.back()" class="p-2 text-gray-400 hover:bg-gray-200 rounded-full transition-all">
          <ArrowLeft class="w-5 h-5" />
        </button>
        <h1 class="text-xl font-bold text-gray-800 line-clamp-1">{{ note?.title }}</h1>
      </div>

      <div v-if="loading" class="text-center py-12 text-gray-500">
        <div class="animate-spin w-8 h-8 border-2 border-indigo-500 border-t-transparent rounded-full mx-auto mb-4"></div>
        加载中...
      </div>

      <div v-else-if="error" class="text-center py-12">
        <AlertCircle class="w-12 h-12 mx-auto mb-4 text-red-300" />
        <p class="text-gray-500 mb-4">{{ error }}</p>
        <button @click="$router.push('/notes')" class="text-indigo-600 hover:underline">返回笔记列表</button>
      </div>

      <div v-else-if="note" class="space-y-6">
        <div class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
          <div class="flex items-center justify-between mb-4 pb-4 border-b border-gray-100">
            <div class="flex items-center gap-3">
              <img :src="note.authorAvatar || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + note.authorAccount" 
                   class="w-10 h-10 rounded-full" />
              <div>
                <p class="font-medium text-gray-800">{{ note.authorName || note.authorAccount }}</p>
                <p class="text-xs text-gray-500">{{ note.updateTime?.split('T')[0] }}</p>
              </div>
            </div>
            <span class="rounded-full bg-green-50 px-3 py-1 text-xs font-medium text-green-700">
              {{ note.visibility === 'PUBLIC' ? '公开笔记' : '好友分享' }}
            </span>
          </div>

          <div class="prose prose-sm max-w-none">
            <div v-html="renderMarkdown(note.content)"></div>
          </div>

          <div v-if="note.tags" class="mt-4 pt-4 border-t border-gray-100">
            <div class="flex items-center gap-2 flex-wrap">
              <Tag class="w-4 h-4 text-gray-400" />
              <span v-for="tag in note.tags.split(',')" :key="tag" 
                    class="px-2 py-1 bg-gray-100 text-gray-600 rounded-full text-xs">
                {{ tag.trim() }}
              </span>
            </div>
          </div>
        </div>

        <div v-if="note.summary || note.keyPoints" class="bg-white rounded-2xl p-6 shadow-sm border border-gray-100">
          <h3 class="font-bold text-gray-800 mb-4 flex items-center gap-2">
            <Sparkles class="w-4 h-4 text-indigo-500" /> AI 分析结果
          </h3>
          <div v-if="note.summary" class="mb-4">
            <h4 class="text-sm font-semibold text-gray-700 mb-2">摘要</h4>
            <p class="text-sm text-gray-600">{{ note.summary }}</p>
          </div>
          <div v-if="note.keyPoints">
            <h4 class="text-sm font-semibold text-gray-700 mb-2">关键要点</h4>
            <p class="text-sm text-gray-600 whitespace-pre-wrap">{{ note.keyPoints }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ArrowLeft, AlertCircle, Tag, Sparkles } from 'lucide-vue-next'
import { noteApi } from '../api'
import MarkdownIt from 'markdown-it'

const md = new MarkdownIt()
const renderMarkdown = (text) => md.render(text)

const route = useRoute()
const note = ref(null)
const loading = ref(true)
const error = ref('')

const fetchNote = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await noteApi.getShared(route.params.id)
    if (res.code === 200 && res.data) {
      note.value = res.data
    } else {
      error.value = res.msg || '无法访问该笔记'
    }
  } catch (err) {
    console.error('获取分享笔记失败:', err)
    error.value = '该笔记不存在或您没有访问权限'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchNote()
})
</script>

<style scoped>
.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
