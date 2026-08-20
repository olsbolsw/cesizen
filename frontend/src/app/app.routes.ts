import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        loadComponent: () => import('./features/home/home.component').then((m) => m.HomeComponent),
      },
      {
        path: 'articles',
        loadComponent: () =>
          import('./features/articles/article-list/article-list.component').then(
            (m) => m.ArticleListComponent,
          ),
      },
      {
        path: 'articles/:id',
        loadComponent: () =>
          import('./features/articles/article-detail/article-detail.component').then(
            (m) => m.ArticleDetailComponent,
          ),
      },
      {
        path: 'emotions',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/emotions/emotion-tracker/emotion-tracker.component').then(
            (m) => m.EmotionTrackerComponent,
          ),
      },
      {
        path: 'profile',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/profile/profile.component').then((m) => m.ProfileComponent),
      },
      {
        path: 'admin/articles',
        canActivate: [authGuard, adminGuard],
        loadComponent: () =>
          import('./features/admin/admin-articles/admin-articles.component').then(
            (m) => m.AdminArticlesComponent,
          ),
      },
      {
        path: 'admin/users',
        canActivate: [authGuard, adminGuard],
        loadComponent: () =>
          import('./features/admin/admin-users/admin-users.component').then(
            (m) => m.AdminUsersComponent,
          ),
      },
    ],
  },
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      {
        path: 'login',
        loadComponent: () =>
          import('./features/auth/login/login.component').then((m) => m.LoginComponent),
      },
      {
        path: 'register',
        loadComponent: () =>
          import('./features/auth/register/register.component').then((m) => m.RegisterComponent),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
