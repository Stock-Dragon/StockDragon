#!/bin/bash
set -e
cd "$(dirname "$0")/../../docker/postgres"
docker compose -f postgres-compose.yml up -d