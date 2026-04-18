<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#fafafa] to-[#e0e7ff] p-4 relative overflow-hidden">
    <div class="absolute w-96 h-96 bg-indigo-200/50 rounded-full blur-3xl -top-10 -left-10"></div>
    <div class="absolute w-96 h-96 bg-purple-200/50 rounded-full blur-3xl -bottom-10 -right-10"></div>

    <div class="w-full max-w-md bg-white/60 backdrop-blur-xl border border-white rounded-[1rem] p-8 shadow-[0_8px_30px_rgb(0,0,0,0.04)] z-10">
      <div class="text-center mb-8">
        <div class="w-12 h-12 bg-indigo-600 rounded-[1rem] flex items-center justify-center mx-auto mb-4 shadow-lg shadow-indigo-200">
          <Sparkles class="w-6 h-6 text-white" />
        </div>
        <h1 class="text-2xl font-bold text-gray-800">SmartNote</h1>
        <p class="text-gray-500 text-sm mt-2">极简智能，记录灵感</p>
      </div>

      <form @submit.prevent="handleAuth" class="space-y-5">
        <div class="relative group">
          <User class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
          <input v-model="form.keyword" type="text" placeholder="邮箱 / 手机号 / 账号" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-4 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
        </div>

        <div class="relative group">
          <Lock class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
          <input :type="showPwd ? 'text' : 'password'" v-model="form.password" placeholder="密码（6-20位）" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-12 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
          <button type="button" @click="showPwd = !showPwd" class="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
            <Eye v-if="!showPwd" class="w-4 h-4" />
            <EyeOff v-else class="w-4 h-4" />
          </button>
        </div>

        <transition name="fade">
          <div v-if="!isLogin" class="space-y-4">
            <div class="relative group">
              <User class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
              <input v-model="form.account" type="text" placeholder="账号（必填）" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-4 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
            </div>
            <div class="relative group">
              <Mail class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
              <input v-model="form.email" type="text" placeholder="邮箱（必填）" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-4 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
            </div>
            <div class="relative group">
              <Phone class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
              <input v-model="form.phone" type="tel" placeholder="手机号（必填）" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-4 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
            </div>
            <div class="relative group">
              <CheckCircle class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
              <input v-model="form.confirmPassword" :type="showPwd ? 'text' : 'password'" placeholder="确认密码（6-20位）" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-4 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
            </div>
          </div>
        </transition>

        <button type="submit" :disabled="loading" class="w-full bg-gradient-to-r from-indigo-500 to-indigo-600 text-white font-medium py-3 rounded-full shadow-md shadow-indigo-200 transition-all duration-200 hover:shadow-lg hover:-translate-y-0.5 active:scale-95 disabled:opacity-50">
          {{ loading ? '处理中...' : (isLogin ? '登录' : '注册') }}
        </button>
      </form>

      <div class="mt-6 text-center text-sm">
        <span class="text-gray-500">{{ isLogin ? '还没有账号？' : '已有账号？' }}</span>
        <button @click="toggleMode" class="text-indigo-600 hover:text-indigo-700 font-medium ml-1">{{ isLogin ? '去注册' : '去登录' }}</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Eye, EyeOff, Sparkles, Mail, Phone, CheckCircle } from 'lucide-vue-next'
import request from '../utils/request'

const router = useRouter()
const showPwd = ref(false)
const isLogin = ref(true)
const loading = ref(false)

const form = ref({
  keyword: '',
  password: '',
  account: '',
  email: '',
  phone: '',
  confirmPassword: ''
})

const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
const phoneRegex = /^1[3-9]\d{9}$/

const validateEmail = (email) => {
  return emailRegex.test(email)
}

const validatePhone = (phone) => {
  return phoneRegex.test(phone)
}

const validatePassword = (password) => {
  return password.length >= 6 && password.length <= 20
}

const toggleMode = () => {
  isLogin.value = !isLogin.value
  form.value = {
    keyword: '',
    password: '',
    account: '',
    email: '',
    phone: '',
    confirmPassword: ''
  }
}

const handleAuth = async () => {
  if (isLogin.value) {
    if (!form.value.keyword || !form.value.password) {
      alert('请输入邮箱/手机号/账号和密码')
      return
    }
    if (!validatePassword(form.value.password)) {
      alert('密码长度需在6-20位之间')
      return
    }
  } else {
    if (!form.value.account || !form.value.email || !form.value.phone || !form.value.password || !form.value.confirmPassword) {
      alert('请填写完整注册信息')
      return
    }
    if (!validateEmail(form.value.email)) {
      alert('请输入正确的邮箱格式')
      return
    }
    if (!validatePhone(form.value.phone)) {
      alert('请输入正确的手机号格式（11位数字，以1开头）')
      return
    }
    if (!validatePassword(form.value.password)) {
      alert('密码长度需在6-20位之间')
      return
    }
    if (form.value.password !== form.value.confirmPassword) {
      alert('两次密码输入不一致')
      return
    }
  }

  loading.value = true
  try {
    if (isLogin.value) {
      const payload = {
        keyword: form.value.keyword,
        password: form.value.password
      }
      const res = await request.post('/users/login', payload)
      if (res.code === 200 && res.data) {
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('userInfo', JSON.stringify(res.data))
        router.push('/notes')
      } else {
        alert(res.msg || '登录失败')
      }
    } else {
      const payload = {
        account: form.value.account,
        email: form.value.email,
        phone: form.value.phone,
        password: form.value.password,
        confirmPassword: form.value.confirmPassword
      }
      const res = await request.post('/users/register', payload)
      if (res.code === 200) {
        alert('注册成功，请登录')
        toggleMode()
      } else {
        alert(res.msg || '注册失败')
      }
    }
  } catch (error) {
    console.error('网络请求失败:', error)
    alert('连接服务器失败，请检查后端服务是否启动')
  } finally {
    loading.value = false
  }
}
</script>
