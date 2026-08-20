# CESIZen — Projet CDA CESI

Application web de bien-être : gestion des comptes, articles d'information et tracker d'émotions.

## Stack

| Couche | Technologie |
|--------|-------------|
| Frontend | Angular 19 (standalone), Angular Material, PrimeNG |
| Backend | Spring Boot 3.2, Spring Security, JWT |
| Base de données | PostgreSQL 16 |

## Architecture

```
bloc2 solo/
├── backend/          # API REST Spring Boot (MVC)
│   └── src/main/java/fr/cesi/cesizen/
│       ├── controller/    # Couche présentation REST
│       ├── service/         # Logique métier
│       ├── domain/          # Entités JPA + repositories
│       ├── dto/             # Contrats API
│       ├── mapper/          # Entity ↔ DTO
│       ├── security/        # JWT + Spring Security
│       └── exception/       # Gestion globale des erreurs
├── frontend/         # SPA Angular
│   └── src/app/
│       ├── core/            # Services API, guards, interceptors, models
│       ├── features/        # Modules fonctionnels
│       └── layout/          # Shell applicatif
└── docker-compose.yml
```

## Installation complète

Pour cloner le projet depuis GitHub et l’installer pas à pas (prérequis, Docker, backend, frontend, tests, dépannage), consultez **[GUIDE-INSTALLATION.md](./GUIDE-INSTALLATION.md)**.

## Démarrage rapide

### 1. PostgreSQL

```bash
docker compose up -d
```

### 2. Backend

Maven n'a pas besoin d'être installé globalement : utilisez le **Maven Wrapper** inclus.

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

*(Linux/macOS : `./mvnw spring-boot:run`)*

Si vous préférez `mvn` : installez Maven (`winget install Apache.Maven`) et ajoutez-le au PATH.

API : `http://localhost:8080`

Compte admin de démo : `admin@cesizen.fr` / `Admin123!`

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

Application : `http://localhost:4200`

## Endpoints principaux

| Méthode | URL | Accès |
|---------|-----|-------|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET | `/api/articles/published` | Public |
| GET/POST | `/api/emotions` | USER authentifié |
| GET | `/api/emotions/stats` | USER authentifié |
| CRUD | `/api/articles` | ADMIN |
| GET | `/api/admin/users` | ADMIN |

## RGPD (bonnes pratiques intégrées)

- Consentement obligatoire à l'inscription (`rgpdConsent`)
- Horodatage du consentement
- Droit à l'effacement : désactivation du compte via `DELETE /api/users/me`
- Données émotionnelles isolées par utilisateur
- Messages d'erreur génériques côté serveur (`application.yml`)

## Modules métier

1. **Comptes utilisateurs** — inscription, connexion JWT, profil, rôles USER/ADMIN
2. **Articles** — publication d'informations bien-être
3. **Tracker d'émotions** — saisie, historique, statistiques (graphique PrimeNG)

## Variables d'environnement

| Variable | Description |
|----------|-------------|
| `JWT_SECRET` | Clé HMAC JWT (production) |

## Prochaines étapes suggérées (projet académique)

- Tests unitaires (JUnit + Jasmine)
- Pagination API
- Export des données personnelles (portabilité RGPD)
- CI/CD GitHub Actions
