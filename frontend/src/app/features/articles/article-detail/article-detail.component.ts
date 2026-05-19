import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ArticleService } from '../../../core/services/article.service';
import { Article } from '../../../core/models/article.model';

@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [DatePipe, RouterLink, MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './article-detail.component.html',
  styleUrl: './article-detail.component.scss',
})
export class ArticleDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly articleService = inject(ArticleService);

  article: Article | null = null;
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.articleService.getById(id).subscribe({
      next: (data) => {
        this.article = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message ?? 'Impossible de charger cet article.';
      },
    });
  }
}
