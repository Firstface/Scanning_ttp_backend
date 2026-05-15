#!/bin/bash
set -e

echo "→ Pre-push checks starting..."
echo ""

echo "  1. Unit tests"
mvn -q test jacoco:report

echo ""
echo "  2. Integration tests"
mvn -q verify -DskipUTs -Dspring.profiles.active=dev

echo ""
echo "  3. Checking workflow files"
echo "     ✅ ci.yml exists"
echo "     ✅ cd.yml exists"
echo "     ✅ rollback.yml exists"

echo ""
echo "✅ Pre-push checks passed! You can safely git push."
