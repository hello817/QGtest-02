import request from '../utils/request'

export const userApi = {
  login(data) {
    return request.post('/users/login', data)
  },

  register(data) {
    return request.post('/users/register', data)
  },

  getInfo() {
    return request.get('/users/info')
  },

  updatePassword(data) {
    return request.put('/users/password', data)
  },

  updateMotto(data) {
    return request.put('/users/motto', data)
  },

  updateUsername(data) {
    return request.put('/users/username', data)
  },

  updateAvatar(formData) {
    return request.put('/users/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  searchUser(keyword) {
    return request.get('/users/search', { params: { keyword } })
  }
}
