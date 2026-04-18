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

      <form @submit.prevent="handleLogin" class="space-y-5">
        <div class="relative group">
          <User class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
          <input type="text" placeholder="邮箱或手机号" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-4 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
        </div>

        <div class="relative group">
          <Lock class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-indigo-600 transition-colors" />
          <input :type="showPwd ? 'text' : 'password'" placeholder="密码" class="w-full bg-white/50 border border-gray-200 rounded-full py-3 pl-12 pr-12 text-gray-700 outline-none transition-all duration-200 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white" />
          <button type="button" @click="showPwd = !showPwd" class="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
            <Eye v-if="!showPwd" class="w-4 h-4" />
            <EyeOff v-else class="w-4 h-4" />
          </button>
        </div>

        <button @click="$router.push('/notes')" type="button" class="w-full bg-gradient-to-r from-indigo-500 to-indigo-600 text-white font-medium py-3 rounded-full shadow-md shadow-indigo-200 transition-all duration-200 hover:shadow-lg hover:-translate-y-0.5 active:scale-95">
          登录
        </button>
      </form>

      <div class="mt-6 text-center text-sm">
        <span class="text-gray-500">还没有账号？</span>
        <a href="#" class="text-indigo-600 hover:text-indigo-700 font-medium ml-1">去注册</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { User, Lock, Eye, EyeOff, Sparkles } from 'lucide-vue-next';
import request from '../utils/request'; // 引入我们封装的 axios

const router = useRouter();
const isLogin = ref(true); // 切换登录/注册状态
const showPwd = ref(false);

const form = ref({
  account: '',
  password: ''
});

const handleAuth = async () => {
  if (!form.value.account || !form.value.password) {
    alert('请输入账号和密码');
    return;
  }

  try {
    if (isLogin.value) {
      // 🌟 对接真实登录接口
      const res = await request.post('/users/login', form.value);
      if (res.code === 200) {
        localStorage.setItem('token', res.data); // 保存 Token
        router.push('/notes'); // 跳转主页
      } else {
        alert(res.msg || '登录失败');
      }
    } else {
      // 🌟 对接真实注册接口
      const res = await request.post('/users/register', form.value);
      if (res.code === 200) {
        alert('注册成功，请登录');
        isLogin.value = true;
      } else {
        alert(res.msg || '注册失败');
      }
    }
  } catch (error) {
    console.error('网络请求失败:', error);
    alert('连接服务器失败，请检查 Java 后端是否启动');
  }
};
</script>