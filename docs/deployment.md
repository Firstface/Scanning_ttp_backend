# Local Compose Deployment

Status: **Configured but not executed** on the current host.

`deploy/docker-compose.yml` defines a production-like local topology: Nginx, React frontend, Spring Boot backend, MySQL 8.4, Flyway startup migrations, bridge network and health checks. It expects immutable image tags through `IMAGE_TAG`; the rollback command is:

```bash
sh scripts/rollback-compose.sh <immutable-image-tag> production-like
```

This host does not have Docker, so no local staging or production-like deployment result is claimed. Run the Compose commands on a Docker-capable host, preserve the resulting logs in `reports/deployment/`, and only then update the execution status.
