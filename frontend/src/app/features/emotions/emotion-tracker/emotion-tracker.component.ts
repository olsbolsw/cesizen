import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DatePipe, DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { DropdownModule } from 'primeng/dropdown';
import { ChartModule } from 'primeng/chart';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { EmotionService } from '../../../core/services/emotion.service';
import {
  EmotionEntry,
  EmotionStats,
  EmotionType,
  EmotionTypeInfo,
} from '../../../core/models/emotion.model';

@Component({
  selector: 'app-emotion-tracker',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DatePipe,
    DecimalPipe,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    DropdownModule,
    ChartModule,
    TableModule,
    ButtonModule,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './emotion-tracker.component.html',
  styleUrl: './emotion-tracker.component.scss',
})
export class EmotionTrackerComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly emotionService = inject(EmotionService);
  private readonly messages = inject(MessageService);

  emotionTypes: EmotionTypeInfo[] = [];
  emotionOptions: { label: string; value: EmotionType }[] = [];

  editingEntryId: number | null = null;

  readonly form = this.fb.nonNullable.group({
    emotionType: [EmotionType.CALME, Validators.required],
    intensity: [5, [Validators.required, Validators.min(1), Validators.max(10)]],
    note: [''],
    entryDate: [new Date().toISOString().slice(0, 10)],
  });

  entries: EmotionEntry[] = [];
  stats: EmotionStats | null = null;
  chartData: { labels: string[]; datasets: { data: number[]; backgroundColor: string[] }[] } | null =
    null;

  ngOnInit(): void {
    this.emotionService.getTypes().subscribe({
      next: (types) => {
        this.emotionTypes = types;
        this.emotionOptions = types.map((t) => ({ label: t.label, value: t.code }));
      },
    });
    this.reload();
  }

  get formTitle(): string {
    return this.editingEntryId ? 'Modifier une entrée' : 'Nouvelle entrée';
  }

  labelFor(code: EmotionType): string {
    return this.emotionTypes.find((t) => t.code === code)?.label ?? code;
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    const raw = this.form.getRawValue();
    const payload = {
      emotionType: raw.emotionType,
      intensity: raw.intensity,
      note: raw.note || undefined,
      entryDate: raw.entryDate,
    };

    const request$ = this.editingEntryId
      ? this.emotionService.update(this.editingEntryId, payload)
      : this.emotionService.create(payload);

    request$.subscribe({
      next: () => {
        this.messages.add({
          severity: 'success',
          summary: 'Émotions',
          detail: this.editingEntryId ? 'Entrée mise à jour' : 'Entrée enregistrée',
        });
        this.cancelEdit();
        this.reload();
      },
    });
  }

  startEdit(entry: EmotionEntry): void {
    this.editingEntryId = entry.id;
    this.form.patchValue({
      emotionType: entry.emotionType,
      intensity: entry.intensity,
      note: entry.note ?? '',
      entryDate: entry.entryDate,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit(): void {
    this.editingEntryId = null;
    this.form.reset({
      emotionType: EmotionType.CALME,
      intensity: 5,
      note: '',
      entryDate: new Date().toISOString().slice(0, 10),
    });
  }

  deleteEntry(id: number): void {
    this.emotionService.delete(id).subscribe({
      next: () => {
        if (this.editingEntryId === id) {
          this.cancelEdit();
        }
        this.messages.add({ severity: 'info', summary: 'Émotions', detail: 'Entrée supprimée' });
        this.reload();
      },
    });
  }

  private reload(): void {
    this.emotionService.listMine().subscribe({
      next: (data) => (this.entries = data),
    });
    this.emotionService.getStats().subscribe({
      next: (stats) => {
        this.stats = stats;
        const labels = this.emotionTypes.length
          ? this.emotionTypes.map((t) => t.label)
          : Object.keys(stats.countByEmotion);
        const keys = this.emotionTypes.length
          ? this.emotionTypes.map((t) => t.code)
          : (Object.keys(stats.countByEmotion) as EmotionType[]);
        this.chartData = {
          labels,
          datasets: [
            {
              data: keys.map((k) => stats.countByEmotion[k] ?? 0),
              backgroundColor: [
                '#14b8a6',
                '#0ea5e9',
                '#f97316',
                '#6366f1',
                '#ef4444',
                '#a855f7',
                '#64748b',
                '#22c55e',
              ],
            },
          ],
        };
      },
    });
  }
}
