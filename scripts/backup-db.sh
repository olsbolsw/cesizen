#!/usr/bin/env bash
set -euo pipefail

ENV_FILE="${1:-.env.prod}"
BACKUP_DIR="${BACKUP_DIR:-backups}"
mkdir -p "$BACKUP_DIR"

set -a
source "$ENV_FILE"
set +a

STAMP="$(date +%Y%m%d_%H%M%S)"
OUT="$BACKUP_DIR/cesizen_${STAMP}.sql.gz"

printf 'Creating PostgreSQL backup: %s\n' "$OUT"
docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" exec -T db \
  pg_dump -U "$POSTGRES_USER" "$POSTGRES_DB" | gzip > "$OUT"

printf 'Backup completed: %s\n' "$OUT"
