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

  trash(id) {
    return request.put(`/notes/${id}/trash`)
  },

  setVisibility(id, visibility) {
    return request.put(`/notes/${id}/visibility`, null, { params: { shareLevel: visibility } })
  },

  analyze(id) {
    return request.post(`/notes/${id}/analyze`)
  },

  getHistory() {
    return request.get('/notes/history')
  },

  getShared(noteId) {
    return request.get(`/notes/share/${noteId}`)
  },

  getSharedNotes(type) {
    return request.get('/notes/shared', { params: { type } })
  },

  getTrash() {
    return request.get('/notes/trash')
  },

  restore(id) {
    return request.put(`/notes/${id}/restore`)
  },

  emptyTrash() {
    return request.delete('/notes/trash')
  }
}
