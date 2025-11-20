import api from './api'; // ton axios personnalisé

// Définition des types TypeScript
interface NumeroComplet {
  id: number;
  valeurNumero: string;
  proprietaire: string;
  plateformes: string[];
}

// Service pour récupérer les numéros de l'utilisateur connecté
const getMesNumeros = async (): Promise<NumeroComplet[]> => {
  try {
    const response = await api.get('/api/numeros-overview/mes-numeros');
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des numéros :', error);
    throw error;
  }
};

// Récupérer les numéros de l'utilisateur pour une plateforme spécifique
const getMesNumerosParPlateforme = async (plateformeId: number): Promise<NumeroComplet[]> => {
  try {
    const response = await api.get(`/api/numeros-overview/mes-numeros/plateforme/${plateformeId}`);
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des numéros par plateforme :', error);
    throw error;
  }
};

// Récupérer tous les numéros d'une plateforme (pour admin ou vue globale)
const getNumerosParPlateforme = async (plateformeId: number): Promise<NumeroComplet[]> => {
  try {
    const response = await api.get(`/api/numeros-overview/plateforme/${plateformeId}`);
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des numéros de la plateforme :', error);
    throw error;
  }
};

// Récupérer tous les numéros (admin seulement)
const getAllNumeros = async (): Promise<NumeroComplet[]> => {
  try {
    const response = await api.get('/api/numeros-overview');
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération de tous les numéros :', error);
    throw error;
  }
};

export default {
  getMesNumeros,
  getMesNumerosParPlateforme,
  getNumerosParPlateforme,
  getAllNumeros
};