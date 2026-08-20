import { Role } from './role.enum';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  rgpdConsent: boolean;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
}

export interface AuthUser {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  token: string;
}
