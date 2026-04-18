import request from '../utils/request'

export const friendApi = {
  getList() {
    return request.get('/friends')
  },

  getRequests() {
    return request.get('/friends/requests')
  },

  sendRequest(friendId, groupTag = '默认') {
    return request.post(`/friends/requests/${friendId}`, null, { params: { groupTag } })
  },

  acceptRequest(requestId) {
    return request.put(`/friends/requests/${requestId}/accept`)
  },

  rejectRequest(requestId) {
    return request.put(`/friends/requests/${requestId}/reject`)
  },

  delete(id) {
    return request.delete(`/friends/${id}`)
  },

  updateGroup(id, groupTag) {
    return request.put(`/friends/${id}/group`, { groupTag })
  },

  searchUser(keyword) {
    return request.get('/users/search', { params: { keyword } })
  }
}
