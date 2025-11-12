import api from './api' // ton instance axios

interface MessagePayload {
  expediteur: string
  destinataire: string
  messageTexte: {
    contenu: string
  }
}

interface MessageResponse {
  status: string
  errorMessage?: string
  [key: string]: any
}

export const sendMessage = async (payload: MessagePayload, platform: string): Promise<MessageResponse> => {
  try {
    const endpoint =
      platform === 'whatsapp'
        ? '/api/whatsapp/send'
        : '/api/sms/send'

    const response = await api.post(endpoint, payload)

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
