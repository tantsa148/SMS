import api from './api'

export interface MessagePayload {
  destinataire: string
  messageTexte: {
    contenu: string
  }
  idNumero: number
}

export interface MessageResponse {
  status: string
  errorMessage?: string
  [key: string]: any
}

export const sendMessage = async (payload: MessagePayload): Promise<MessageResponse> => {
  try {
    const response = await api.post('/api/sms/send', payload)

    // Normaliser la réponse pour la vue
    return {
      status: response.data?.status || 'success',
      errorMessage: response.data?.errorMessage || '',
      ...response.data
    }
  } catch (error: any) {
    console.error('Erreur lors de l’envoi du message :', error)

    // Normaliser l’erreur pour la vue
    if (error.response && error.response.data) {
      return {
        status: 'failed',
        errorMessage: error.response.data.errorMessage || error.response.data.message || 'Erreur inconnue'
      }
    }

    return {
      status: 'failed',
      errorMessage: error.message || 'Erreur inconnue'
    }
  }
}
