# Help
.PHONY: help
help:
	@echo "Hive Sampling Mock - Makefile"
	@echo ""
	@echo "Usage:"
	@echo "  make up          - Start all services"
	@echo "  make down        - Stop all services"
	@echo "  make logs        - Show service logs"
	@echo "  make test        - Run all tests"
	@echo "  make e2e         - Run E2E tests"
	@echo "  make reset       - Reset everything (down + up)"
	@echo "  make build       - Build all Docker images"
	@echo ""

# Start all services
.PHONY: up
up:
	@cd deploy && docker-compose up -d
	@echo "Services started. Waiting for health..."
	@sleep 10
	@cd deploy && docker-compose ps

# Stop all services
.PHONY: down
down:
	@cd deploy && docker-compose down -v
	@echo "All services stopped."

# Show logs
.PHONY: logs
logs:
	@cd deploy && docker-compose logs -f

# Run all tests
.PHONY: test
test:
	@mvn clean test jacoco:report

# Run E2E tests (requires running services)
.PHONY: e2e
e2e:
	@echo "E2E Tests: This will be implemented with Playwright"
	@echo "Running E2E tests..."
	@cd tests/e2e && npx playwright test || true

# Reset everything
.PHONY: reset
reset: down
	@echo "Resetting demo environment..."
	@make up

# Build Docker images
.PHONY: build
build:
	@cd deploy && docker-compose build --no-cache
