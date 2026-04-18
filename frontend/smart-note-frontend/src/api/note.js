import request from '../utils/request'

export const noteApi = {
  getList(params) {
    return request.get('/notes', { params })
  },

  getDetail(id) {
    return request.get(`/notes/${id}`)
  },

  create(data) {
    return request.post('/notes', data)
  },

  update(id, data) {
    return request.put(`/notes/${id}`, data)
  },

  delete(id) {
    return request.delete(`/notes/${id}`)
  },

  analyze(id) {
    return request.post(`/notes/${id}/analyze`)
  },

  getHistory() {
    return request.get('/notes/history')
  },

  getShared(shareId) {
    return request.get(`/share/${shareId}`)
  },

  getShareInfo(noteId) {
    return request.get(`/share/note/${noteId}`)
  },

  createShare(data) {
    return request.post('/share', data)
  }
}
