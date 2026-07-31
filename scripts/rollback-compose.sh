set -eu
TAG=${1:?usage: sh rollback-compose.sh <immutable-image-tag>}
PROFILE=${2:-production-like}
: "${BACKEND_IMAGE:=scanning-ttp-backend}"
: "${FRONTEND_IMAGE:=scanning-ttp-frontend}"
export IMAGE_TAG="$TAG" SPRING_PROFILE="$PROFILE" BACKEND_IMAGE FRONTEND_IMAGE
cd "$(dirname "$0")/../deploy"
if docker image inspect "${BACKEND_IMAGE}:${TAG}" > .rollback-backend-image-inspect.txt 2>&1 && docker image inspect "${FRONTEND_IMAGE}:${TAG}" > .rollback-frontend-image-inspect.txt 2>&1; then
  :
else
  docker compose pull backend frontend
fi
docker compose up -d --no-build --wait mysql backend frontend nginx-proxy
curl --fail --silent --show-error "http://localhost:${HTTP_PORT:-8088}/actuator/health"
printf 'rollback completed: image tag=%s profile=%s\n' "$TAG" "$PROFILE"
