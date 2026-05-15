#!/bin/bash
# Demo Reset Script

set -e

echo "==================================="
echo "  Hive Sampling Demo - Reset"
echo "==================================="
echo ""

cd deploy

echo "1. Stopping all containers..."
docker-compose down -v || true

echo ""
echo "2. Cleaning up..."
rm -rf reports/ 2>/dev/null || true
docker system prune -f 2>/dev/null || true

echo ""
echo "3. Starting fresh..."
docker-compose up -d --build

echo ""
echo "4. Waiting for services to be ready..."
sleep 15

echo ""
echo "5. Checking health..."
curl -f http://localhost:8080/actuator/health || (echo "⚠️ Backend not ready yet, waiting more..." && sleep 10)

echo ""
echo "==================================="
echo "  ✅ Demo environment ready!"
echo "==================================="
echo ""
echo "Frontend: http://localhost:80"
echo "Backend:  http://localhost:8080"
echo ""
echo "Useful commands:"
echo "  make logs     - Tail logs"
echo "  make reset    - Reset again"
echo ""
