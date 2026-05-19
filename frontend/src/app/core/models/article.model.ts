export interface Article {
  id: number;
  title: string;
  content: string;
  category: string | null;
  published: boolean;
  authorName: string;
  createdAt: string;
  updatedAt: string;
}

export interface ArticleRequest {
  title: string;
  content: string;
  category?: string;
  published: boolean;
}
