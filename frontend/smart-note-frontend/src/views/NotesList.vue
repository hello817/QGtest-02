<script setup>
import { ref, onMounted } from 'vue';
import request from '../utils/request';
// ... 其他图标引入

const notes = ref([]); // 存放从后端拿到的数据

const fetchNotes = async () => {
  // 对接 /api/notes/page 接口
  const res = await request.get('/notes/page', { params: { page: 1, pageSize: 10 } });
  if (res.code === 200) {
    notes.value = res.data.records; // 假设返回的是分页对象
  }
};

onMounted(() => {
  fetchNotes();
});
</script>

<template>
  <div v-for="note in notes" :key="note.id" @click="$router.push(`/note/${note.id}`)" class="...">
    <h3 class="font-bold text-gray-800">{{ note.title }}</h3>
    <p class="text-sm text-gray-500 line-clamp-3">{{ note.content }}</p>
    </div>
</template>