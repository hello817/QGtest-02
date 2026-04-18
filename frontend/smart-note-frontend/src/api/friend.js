import request from '../utils/request'

export const friendApi = {
  getList() {
    return request.get('/friends')
  },

  getRequests() {
    return request.get('/friends/requests')
  },

  sendRequest(data) {
    return request.put('/friends/request', data)
  },

  handleRequest(id, action) {
    return request.put(`/friends/request/${id}`, { action })
  },

  delete(id) {
    return request.delete(`/friends/${id}`)
  },

  updateGroup(id, group) {
    return request.put(`/friends/${id}/group`, { group })
  },

  searchUser(keyword) {
    return request.get('/users/search', { params: { keyword } })
  }
}
