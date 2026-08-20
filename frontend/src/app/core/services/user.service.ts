import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UpdateProfileRequest, User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly baseUrl = `${environment.apiUrl}/users/me`;

  constructor(private readonly http: HttpClient) {}

  getProfile() {
    return this.http.get<User>(this.baseUrl);
  }

  updateProfile(request: UpdateProfileRequest) {
    return this.http.put<User>(this.baseUrl, request);
  }

  deleteAccount() {
    return this.http.delete<void>(this.baseUrl);
  }
}
