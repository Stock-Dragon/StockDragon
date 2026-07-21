$ErrorActionPreference = "Stop"

Set-Location "$PSScriptRoot\..\..\docker\postgres"
docker compose -f postgres-compose.yml up -d

Write-Host "Waiting for Postgres to be ready..."
do {
    Start-Sleep -Seconds 1
    docker exec stockdragon-postgres pg_isready -U stockdragon | Out-Null
} while ($LASTEXITCODE -ne 0)

Set-Location "..\flyway"
docker compose -f flyway-compose.yml run --rm flyway