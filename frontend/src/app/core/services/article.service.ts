import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Article, ArticleRequest } from '../models/article.model';

@Injectable({ providedIn: 'root' })
export class ArticleService {
  private readonly baseUrl = `${environment.apiUrl}/articles`;

  constructor(private readonly http: HttpClient) {}

  getPublished() {
    return this.http.get<Article[]>(`${this.baseUrl}/published`);
  }

  getAll() {
    return this.http.get<Article[]>(this.baseUrl);
  }

  getById(id: number) {
    return this.http.get<Article>(`${this.baseUrl}/${id}`);
  }

  create(request: ArticleRequest) {
    return this.http.post<Article>(this.baseUrl, request);
  }

  update(id: number, request: ArticleRequest) {
    return this.http.put<Article>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
