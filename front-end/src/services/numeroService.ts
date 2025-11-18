// src/services/numeroService.ts
import api from './api'

// Réponse réelle que ton backend renvoie actuellement
export interface NumeroAssignResponse {
  idUtilisateur: number
  username: string
  idNumero?: number
  valeurNumero: string
  dateCreation?: string | null
  messages: string[]                      // Le champ magique
}

// DTO envoyé (inchangé)
export interface CreateNumeroDTO {
  valeurNumero: string
  idPlateforme: number
}

export const createNumeroAndAssign = async (
  dto: CreateNumeroDTO
): Promise<NumeroAssignResponse> => {
  const response = await api.post<NumeroAssignResponse>(
    '/api/numero-assign/create',
    dto
  )
  return response.data
}