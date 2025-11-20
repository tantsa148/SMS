import api from './api'; // ton axios personnalisé
import type { UserDTO } from '../types/user'; // optionnel : interface TS pour le DTO

export const getCurrentUser = async (): Promise<UserDTO | null> => {
  try {
    const response = await api.get('/api/users/me');
    return response.data as UserDTO;
  } catch (error: any) {
    console.error('Erreur lors de la récupération de l’utilisateur connecté :', error.response?.data || error.message);
    return null;
  }
};
