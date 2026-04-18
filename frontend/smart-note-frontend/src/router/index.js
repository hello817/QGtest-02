import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import NotesList from '../views/NotesList.vue'
import NoteDetail from '../views/NoteDetail.vue'
import Profile from '../views/Profile.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login, meta: { title: '登录', public: true } },
  { path: '/register', component: Login, meta: { title: '注册', public: true } },
  { path: '/notes', component: NotesList, meta: { title: '笔记列表' } },
  { path: '/note/:id', component: NoteDetail, meta: { title: '笔记详情' } },
  { path: '/profile', component: Profile, meta: { title: '个人中心' } },
  { path: '/logout', redirect: () => {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    return '/login'
  }}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) document.title = `${to.meta.title} - SmartNote`
  const publicPages = ['/login', '/register']
  const token = localStorage.getItem('token')
  if (!publicPages.includes(to.path) && !token) {
    return next('/login')
  }
  next()
})

export default router
