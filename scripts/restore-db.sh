#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 backups/cesizen_YYYYMMDD_HHMMSS.sql.gz [.env.prod]"
  exit 1
fi

BACKUP="$1"
ENV_FILE="${2:-.env.prod}"

set -a
source "$ENV_FILE"
set +a

read -r -p "This will restore database '$POSTGRES_DB'. Continue? [y/N] " answer
[[ "$answer" =~ ^[Yy]$ ]] || exit 1

gzip -dc "$BACKUP" | docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" exec -T db \
  psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"

echo "Restore completed."
