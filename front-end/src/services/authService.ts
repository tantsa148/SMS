import axios from 'axios'

export interface LoginData {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  // ajouter d'autres infos si l'API renvoie plus
}

export const login = async (data: LoginData): Promise<LoginResponse> => {
  const response = await axios.post<LoginResponse>('http://localhost:8080/login', data)
  console.log('Token reçu depuis API :', response.data.token)
  return response.data
}
