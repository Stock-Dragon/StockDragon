#!/bin/bash
set -e

cd "$(dirname "$0")/../../docker/postgres"
docker compose -f postgres-compose.yml down -v
docker compose -f postgres-compose.yml up -d

echo "Waiting for Postgres to be ready..."
until docker exec stockdragon-postgres pg_isready -U stockdragon >/dev/null 2>&1; do
  sleep 1
done

cd ../flyway
docker compose -f flyway-compose.yml run --rm flyway