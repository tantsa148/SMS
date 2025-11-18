export interface NumeroAssignResponse {
  idUtilisateur: number
  username: string
  idNumero: number
  valeurNumero: string
  dateCreation: string | null
  messages: string[]           // ← le tableau qu’on veut afficher
}