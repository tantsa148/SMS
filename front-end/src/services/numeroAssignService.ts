import api from './api'

export interface NumeroAssigne {
  idUtilisateur: number
  username: string
  idNumero: number
  valeurNumero: string
  dateCreation: string
  messages: any[]
}

export const getNumerosAssignes = async (): Promise<NumeroAssigne[]> => {
  try {
    const response = await api.get('/api/numero-assign/numeros')
    return response.data
  } catch (error) {
    console.error('Erreur lors du chargement des numéros assignés :', error)
    throw error
  }
}
