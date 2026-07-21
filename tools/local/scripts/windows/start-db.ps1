$ErrorActionPreference = "Stop"
Set-Location "$PSScriptRoot\..\..\docker\postgres"
docker compose -f postgres-compose.yml up -d