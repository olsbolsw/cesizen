$ErrorActionPreference = "Stop"
New-Item -ItemType Directory -Force -Path "certs" | Out-Null
$certPath = (Resolve-Path "certs").Path

docker run --rm `
  -v "${certPath}:/certs" `
  alpine:3.23 sh -c 'apk add --no-cache openssl >/dev/null && openssl req -x509 -nodes -newkey rsa:2048 -days 365 -keyout /certs/privkey.pem -out /certs/fullchain.pem -subj "/CN=localhost" -addext "subjectAltName=DNS:localhost,IP:127.0.0.1"'

Write-Host "Self-signed demo certificate created in .\certs"
