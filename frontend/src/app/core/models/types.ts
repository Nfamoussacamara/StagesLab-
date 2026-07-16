export enum StatutTheme {
  EN_ATTENTE = 'EN_ATTENTE',
  VALIDE = 'VALIDE',
  REJETE = 'REJETE',
  SOUTENU = 'SOUTENU'
}

export interface Etudiant {
  id?: number;
  matricule: string;
  nom: string;
  prenom: string;
  email: string;
  niveau: string;
}

export interface Encadreur {
  id?: number;
  nom: string;
  email: string;
  specialite: string;
  disponibilite: number;
}

export interface Theme {
  id?: number;
  titre: string;
  description: string;
  domaine: string;
  statut: StatutTheme;
  etudiantId: number;
  etudiantMatricule: string;
  etudiantNom: string;
  etudiantPrenom: string;
  encadreurId: number;
  encadreurNom: string;
  encadreurSpecialite: string;
}

export interface ThemeRequest {
  titre: string;
  description: string;
  domaine: string;
  statut: StatutTheme;
  etudiantId: number;
  encadreurId: number;
}

export interface Dashboard {
  totalEtudiants: number;
  totalEncadreurs: number;
  totalThemes: number;
  themesEnAttente: number;
  themesValides: number;
  themesSoutenus: number;
  themesRejetes: number;
}
