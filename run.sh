#!/bin/bash
set -e

echo "======================================"
echo "Starting Todo App Docker Setup..."
echo "======================================"

echo "Launching containers with docker-compose..."
docker-compose up -d --build

echo "Services are running:"
echo "   - Frontend: http://localhost:3000"
echo "   - Backend:  http://localhost:8080"
echo "   - Database: accessible internally at db:5432"
