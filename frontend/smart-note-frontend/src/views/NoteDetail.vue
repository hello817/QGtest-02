<template>
  <div class="min-h-screen bg-[#fafafa] p-6">
    <div class="max-w-6xl mx-auto flex gap-6 items-start">
      <div class="flex-1 bg-white rounded-[1rem] p-8 shadow-sm border border-gray-100 min-h-[85vh]">
        <div class="flex items-center gap-4 mb-6">
          <button @click="$router.back()" class="p-2 text-gray-400 hover:bg-gray-100 rounded-full transition-all"><ArrowLeft class="w-5 h-5" /></button>
          <input type="text" value="API 接口设计规范" class="text-2xl font-bold w-full border-none focus:ring-0 p-0 text-gray-800 bg-transparent" />
        </div>
        
        <div class="prose max-w-none text-gray-600 leading-loose text-sm">
          <p>这是一个 Markdown 预览区域的占位符。在此处渲染笔记的具体内容...</p>
        </div>

        <div class="mt-10 pt-6 border-t border-gray-50 flex justify-between items-center">
          <span class="text-xs text-gray-400">最后修改: 2026-04-18</span>
          <select class="text-xs bg-gray-50 border-none rounded-full py-1.5 px-3 focus:ring-0 cursor-pointer">
            <option>仅自己可见</option>
            <option>所有人可见</option>
          </select>
        </div>
      </div>

      <aside class="w-80 sticky top-6 space-y-4">
        <div class="bg-indigo-600 rounded-[1rem] p-6 text-white shadow-lg shadow-indigo-200/50 relative overflow-hidden">
          <div class="absolute right-0 top-0 w-32 h-32 bg-white/10 rounded-full blur-2xl transform translate-x-1/2 -translate-y-1/2"></div>
          <h3 class="font-bold mb-2 flex items-center gap-2"><Sparkles class="w-4 h-4"/> AI 智能分析</h3>
          <p class="text-xs text-indigo-100 mb-4 opacity-90">一键提取核心要点与摘要</p>
          <button class="w-full py-2 bg-white text-indigo-600 rounded-full text-sm font-bold shadow-sm hover:scale-105 active:scale-95 transition-all">开始分析</button>
        </div>

        <div class="bg-white rounded-[1rem] p-5 shadow-sm border border-gray-100">
          <h4 class="text-sm font-bold text-gray-800 mb-3 flex items-center gap-2">
            <div class="w-1 h-3 bg-indigo-500 rounded-full"></div>内容摘要
          </h4>
          <p class="text-xs text-gray-500 leading-relaxed bg-gray-50 p-3 rounded-lg">笔记重点讨论了 RESTful 规范下状态码的标准化处理流程。</p>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup>
import { ArrowLeft, Sparkles } from 'lucide-vue-next';
</script>