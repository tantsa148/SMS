import api from './api'
import type { Plateforme } from '../types/Plateforme'

// GET toutes les plateformes
export async function fetchPlateformes(): Promise<Plateforme[]> {
  const response = await api.get<Plateforme[]>('api/plateformes')
  return response.data
}

// GET par id
export async function fetchPlateformeById(id: number): Promise<Plateforme> {
  const response = await api.get<Plateforme>(`api/plateformes/${id}`)
  return response.data
}

// CREATE
export async function createPlateforme(nomPlateform: string): Promise<Plateforme> {
  const response = await api.post<Plateforme>('api/plateformes', { nomPlateform })
  return response.data
}

// UPDATE
export async function updatePlateforme(id: number, nomPlateform: string): Promise<Plateforme> {
  const response = await api.put<Plateforme>(`api/plateformes/${id}`, { nomPlateform })
  return response.data
}

// DELETE
export async function deletePlateforme(id: number): Promise<void> {
  await api.delete(`api/plateformes/${id}`)
}
