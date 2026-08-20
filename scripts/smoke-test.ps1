param(
    [string]$FrontendUrl = "http://localhost:8080",
    [string]$BackendUrl = "http://localhost:8081"
)
$ErrorActionPreference = "Stop"

Write-Host "1/3 Frontend: $FrontendUrl"
Invoke-WebRequest -UseBasicParsing "$FrontendUrl/" | Out-Null

Write-Host "2/3 Backend health"
$health = Invoke-RestMethod "$BackendUrl/actuator/health"
if ($health.status -ne "UP") { throw "Backend is not UP" }

Write-Host "3/3 Public API endpoint"
Invoke-WebRequest -UseBasicParsing "$FrontendUrl/api/articles/published" | Out-Null
Write-Host "Smoke tests OK"
