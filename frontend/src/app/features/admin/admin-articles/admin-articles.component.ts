import { Component, OnInit, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ArticleService } from '../../../core/services/article.service';
import { Article } from '../../../core/models/article.model';

@Component({
  selector: 'app-admin-articles',
  standalone: true,
  imports: [
    DatePipe,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatIconModule,
    TableModule,
    ButtonModule,
    TagModule,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './admin-articles.component.html',
  styleUrl: './admin-articles.component.scss',
})
export class AdminArticlesComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly articleService = inject(ArticleService);
  private readonly messages = inject(MessageService);

  articles: Article[] = [];
  editingId: number | null = null;

  readonly form = this.fb.nonNullable.group({
    title: ['', Validators.required],
    content: ['', Validators.required],
    category: [''],
    published: [true],
  });

  ngOnInit(): void {
    this.load();
  }

  get formTitle(): string {
    return this.editingId ? 'Modifier le contenu' : 'Créer un contenu';
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    const payload = this.form.getRawValue();
    const request$ = this.editingId
      ? this.articleService.update(this.editingId, payload)
      : this.articleService.create(payload);

    request$.subscribe({
      next: () => {
        this.messages.add({
          severity: 'success',
          summary: 'Back-office',
          detail: this.editingId ? 'Contenu mis à jour' : 'Contenu créé',
        });
        this.cancelEdit();
        this.load();
      },
    });
  }

  startEdit(article: Article): void {
    this.editingId = article.id;
    this.form.patchValue({
      title: article.title,
      content: article.content,
      category: article.category ?? '',
      published: article.published,
    });
  }

  cancelEdit(): void {
    this.editingId = null;
    this.form.reset({ title: '', content: '', category: '', published: true });
  }

  togglePublished(article: Article): void {
    this.articleService
      .update(article.id, {
        title: article.title,
        content: article.content,
        category: article.category ?? undefined,
        published: !article.published,
      })
      .subscribe({
        next: () => {
          this.messages.add({
            severity: 'info',
            summary: 'Visibilité',
            detail: article.published ? 'Contenu masqué du front-office' : 'Contenu publié',
          });
          this.load();
        },
      });
  }

  delete(id: number): void {
    this.articleService.delete(id).subscribe({
      next: () => {
        if (this.editingId === id) {
          this.cancelEdit();
        }
        this.messages.add({ severity: 'info', summary: 'Back-office', detail: 'Contenu supprimé' });
        this.load();
      },
    });
  }

  private load(): void {
    this.articleService.getAll().subscribe({
      next: (data) => (this.articles = data),
    });
  }
}
