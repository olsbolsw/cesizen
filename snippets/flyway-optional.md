# Flyway: add only after Docker + CI are stable

Your Bloc 3 report proposes Flyway, but this starter deliberately does **not** invent a migration for your existing schema.

Once the current database schema is available:

1. Add `org.flywaydb:flyway-core` and the PostgreSQL Flyway module compatible with your Spring Boot version.
2. Create `backend/src/main/resources/db/migration/`.
3. Baseline the existing schema carefully instead of writing a fake `V1` from memory.
4. From then on, add immutable files such as `V2__add_index.sql`.
5. Test migrations in TEST before PROD.

Do this only after we inspect the real backend and current database creation mechanism.
