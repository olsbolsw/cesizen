#!/usr/bin/env sh
set -eu

FRONTEND_URL="${1:-http://localhost:8080}"
BACKEND_URL="${2:-http://localhost:8081}"

printf '1/3 Frontend: %s\n' "$FRONTEND_URL"
curl -fsS "$FRONTEND_URL/" >/dev/null

printf '2/3 Backend health: %s\n' "$BACKEND_URL/actuator/health"
curl -fsS "$BACKEND_URL/actuator/health" | grep -q '"status":"UP"'

printf '3/3 Public API endpoint\n'
curl -fsS "$FRONTEND_URL/api/articles/published" >/dev/null

printf 'Smoke tests OK\n'
