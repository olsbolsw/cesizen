import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly baseUrl = `${environment.apiUrl}/admin`;

  constructor(private readonly http: HttpClient) {}

  listUsers() {
    return this.http.get<User[]>(`${this.baseUrl}/users`);
  }
}
