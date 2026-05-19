import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe, SlicePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { SkeletonModule } from 'primeng/skeleton';
import { ArticleService } from '../../../core/services/article.service';
import { Article } from '../../../core/models/article.model';

@Component({
  selector: 'app-article-list',
  standalone: true,
  imports: [RouterLink, DatePipe, SlicePipe, MatButtonModule, MatIconModule, SkeletonModule],
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.scss',
})
export class ArticleListComponent implements OnInit {
  private readonly articleService = inject(ArticleService);

  articles: Article[] = [];
  loading = true;

  ngOnInit(): void {
    this.articleService.getPublished().subscribe({
      next: (data) => {
        this.articles = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }
}
