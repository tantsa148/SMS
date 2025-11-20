import api from './api' // ton instance Axios avec le token
export interface SmsPayload {
  idNumero: number
  messageId: number
  destinataire: string
}

export const smsService = {
  sendMessage: async (payload: SmsPayload, platform: string) => {
    // Choisir dynamiquement l’URL selon la plateforme
    let url = ''

    if (platform === 'sms') {
      url = '/api/sms-db/send/numero'
    } else if (platform === 'whatsapp') {
      url = '/api/whatsapp-db/send/numero'
    } else {
      throw new Error(`Plateforme inconnue : ${platform}`)
    }

    // Envoi de la requête
    const response = await api.post(url, payload)
    return response.data
  }
}
