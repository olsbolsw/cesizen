#!/usr/bin/env bash
set -euo pipefail

ENV_FILE="${1:-.env.prod}"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "Missing $ENV_FILE"
  exit 1
fi

# Backup first when an existing DB container is running.
if docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" ps --status running db | grep -q db; then
  ./scripts/backup-db.sh "$ENV_FILE"
fi

echo "Pulling production images..."
docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" pull

echo "Starting production stack..."
docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" up -d --wait --wait-timeout 180

echo "Production containers:"
docker compose -f compose.yaml -f compose.prod.yaml --env-file "$ENV_FILE" ps

echo "HTTPS smoke test (self-signed certificates are accepted for the demo only)..."
curl -kfsS https://localhost/ >/dev/null
curl -kfsS https://localhost/api/articles/published >/dev/null

echo "Production deployment OK"
