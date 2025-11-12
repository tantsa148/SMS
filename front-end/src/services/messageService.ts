import api from './api' // <-- ton fichier axios personnalisé

export const messageService = {
  getAll: async () => {
    const response = await api.get('/api/messages')
    return response.data
  },

  getById: async (id: number) => {
    const response = await api.get(`/api/messages/${id}`)
    return response.data
  },

  send: async (message: any) => {
    const response = await api.post('/api/messages', message)
    return response.data
  },

  delete: async (id: number) => {
    const response = await api.delete(`/api/messages/${id}`)
    return response.data
  },
}
