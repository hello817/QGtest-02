import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8801/api',
  timeout: 5000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['Authorization'] = token
  }
  return config
})

request.interceptors.response.use(response => {
  return response.data
}, error => {
  return Promise.reject(error)
})

export default request