$ErrorActionPreference = "Stop"
if (-not (Test-Path ".env.dev")) {
    Copy-Item ".env.example" ".env.dev"
    Write-Host "Created .env.dev from .env.example"
}
docker compose -f compose.yaml -f compose.dev.yaml --env-file .env.dev up --build -d --wait --wait-timeout 180
docker compose -f compose.yaml -f compose.dev.yaml --env-file .env.dev ps
Write-Host "CESIZen DEV: http://localhost:4200"
