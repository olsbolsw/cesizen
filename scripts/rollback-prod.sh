#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 v1.0.0 [.env.prod]"
  exit 1
fi

VERSION="$1"
ENV_FILE="${2:-.env.prod}"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "Missing $ENV_FILE"
  exit 1
fi

./scripts/backup-db.sh "$ENV_FILE"

# Persist the selected version in .env.prod.
if grep -q '^APP_VERSION=' "$ENV_FILE"; then
  sed -i.bak "s/^APP_VERSION=.*/APP_VERSION=$VERSION/" "$ENV_FILE"
else
  printf '\nAPP_VERSION=%s\n' "$VERSION" >> "$ENV_FILE"
fi

echo "Rolling back application images to $VERSION..."
docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" pull backend frontend
docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" up -d --wait --wait-timeout 180 backend frontend

docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" ps
curl -kfsS https://localhost/ >/dev/null
curl -kfsS https://localhost/api/articles/published >/dev/null

echo "Rollback to $VERSION completed. Database rollback is deliberately separate."
