export enum EmotionType {
  JOYEUX = 'JOYEUX',
  CALME = 'CALME',
  STRESSE = 'STRESSE',
  TRISTE = 'TRISTE',
  ENERVE = 'ENERVE',
  ANXIEUX = 'ANXIEUX',
  FATIGUE = 'FATIGUE',
  MOTIVE = 'MOTIVE',
}

export interface EmotionTypeInfo {
  code: EmotionType;
  label: string;
  description: string;
}

export interface EmotionEntry {
  id: number;
  emotionType: EmotionType;
  intensity: number;
  note: string | null;
  entryDate: string;
  createdAt: string;
}

export interface EmotionEntryRequest {
  emotionType: EmotionType;
  intensity: number;
  note?: string;
  entryDate?: string;
}

export interface EmotionStats {
  totalEntries: number;
  averageIntensity: number;
  countByEmotion: Record<EmotionType, number>;
}
