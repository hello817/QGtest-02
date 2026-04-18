<template>
  <div class="min-h-screen bg-[#fafafa] p-8">
    <div class="max-w-4xl mx-auto space-y-6">
      
      <div class="flex items-center gap-4 mb-8">
        <button @click="$router.back()" class="p-2 text-gray-400 hover:bg-gray-200 rounded-full transition-all"><ArrowLeft class="w-5 h-5" /></button>
        <h1 class="text-2xl font-bold text-gray-800">个人中心</h1>
      </div>

      <div class="bg-white rounded-[1rem] p-8 shadow-sm border border-gray-100">
        <h2 class="text-lg font-bold text-gray-800 mb-6">基本资料</h2>
        <div class="flex gap-8 items-start">
          <div class="relative group cursor-pointer">
            <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix" class="w-24 h-24 rounded-full ring-4 ring-gray-50" />
            <div class="absolute inset-0 bg-black/40 rounded-full opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
              <Camera class="w-6 h-6 text-white" />
            </div>
          </div>
          <div class="flex-1 space-y-4">
            <div class="flex items-center gap-4">
              <label class="w-16 text-sm text-gray-500 text-right">昵称</label>
              <input type="text" value="CodeNinja" class="flex-1 bg-transparent border-b border-gray-200 focus:border-indigo-500 py-1 text-sm outline-none transition-colors" />
              <Edit2 class="w-4 h-4 text-gray-400" />
            </div>
            <div class="flex items-center gap-4">
              <label class="w-16 text-sm text-gray-500 text-right">座右铭</label>
              <input type="text" value="Keep Coding" class="flex-1 bg-transparent border-b border-gray-200 focus:border-indigo-500 py-1 text-sm outline-none transition-colors" />
              <Edit2 class="w-4 h-4 text-gray-400" />
            </div>
            <div class="pt-4 ml-20">
              <button class="bg-indigo-600 text-white px-6 py-2 rounded-full text-sm font-medium hover:bg-indigo-700 active:scale-95 transition-all shadow-sm">保存修改</button>
              <button class="ml-4 text-sm text-indigo-600 hover:underline">修改密码</button>
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
              <input type="text" placeholder="搜索邮箱/手机号" class="bg-gray-50 border-none rounded-full py-1.5 pl-9 pr-4 text-sm focus:ring-2 focus:ring-indigo-500/20" />
            </div>
            <button class="bg-indigo-50 text-indigo-600 px-4 py-1.5 rounded-full text-sm font-medium hover:bg-indigo-100 transition-colors">添加</button>
          </div>
        </div>

        <div class="mb-6 bg-orange-50 rounded-xl p-4 border border-orange-100 flex justify-between items-center">
          <div class="flex items-center gap-3">
            <div class="relative">
              <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Alex" class="w-10 h-10 rounded-full" />
              <div class="absolute -top-1 -right-1 w-3 h-3 bg-red-500 rounded-full border-2 border-orange-50"></div>
            </div>
            <div>
              <p class="text-sm font-bold text-gray-800">王同学 <span class="text-xs font-normal text-gray-500 ml-2">请求添加好友</span></p>
              <p class="text-xs text-gray-500">留言: "同学你好，想请教一下拦截器的问题"</p>
            </div>
          </div>
          <div class="flex gap-2">
            <button class="px-3 py-1 rounded-full text-xs font-medium text-gray-500 border border-gray-200 hover:bg-gray-50">拒绝</button>
            <button class="px-3 py-1 rounded-full text-xs font-medium text-white bg-indigo-600 hover:bg-indigo-700 shadow-sm">同意</button>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div v-for="i in 4" :key="i" class="flex items-center justify-between p-3 rounded-xl border border-gray-50 hover:bg-gray-50 transition-colors group">
            <div class="flex items-center gap-3">
              <img :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${i}`" class="w-10 h-10 rounded-full" />
              <div>
                <p class="text-sm font-medium text-gray-800">好友 {{ i }}</p>
                <span class="text-[10px] px-2 py-0.5 bg-gray-200 text-gray-600 rounded-md">默认分组</span>
              </div>
            </div>
            <div class="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
              <button class="p-1.5 text-gray-400 hover:text-indigo-600"><MessageCircle class="w-4 h-4" /></button>
              <button class="p-1.5 text-gray-400 hover:text-red-500"><Trash2 class="w-4 h-4" /></button>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ArrowLeft, Camera, Edit2, Search, MessageCircle, Trash2 } from 'lucide-vue-next';
</script>