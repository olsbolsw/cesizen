import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import {
  EmotionEntry,
  EmotionEntryRequest,
  EmotionStats,
  EmotionTypeInfo,
} from '../models/emotion.model';

@Injectable({ providedIn: 'root' })
export class EmotionService {
  private readonly baseUrl = `${environment.apiUrl}/emotions`;

  constructor(private readonly http: HttpClient) {}

  listMine() {
    return this.http.get<EmotionEntry[]>(this.baseUrl);
  }

  getTypes() {
    return this.http.get<EmotionTypeInfo[]>(`${this.baseUrl}/types`);
  }

  getStats(start?: string, end?: string) {
    let params = new HttpParams();
    if (start) {
      params = params.set('start', start);
    }
    if (end) {
      params = params.set('end', end);
    }
    return this.http.get<EmotionStats>(`${this.baseUrl}/stats`, { params });
  }

  create(request: EmotionEntryRequest) {
    return this.http.post<EmotionEntry>(this.baseUrl, request);
  }

  update(id: number, request: EmotionEntryRequest) {
    return this.http.put<EmotionEntry>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
