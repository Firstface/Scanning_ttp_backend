terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

provider "docker" {}

# Network
resource "docker_network" "hive_sampling" {
  name = "hive-sampling-network"
}

# Backend Service
resource "docker_image" "backend" {
  name         = "ghcr.io/hive-sampling/backend:latest"
  keep_locally = true
}

resource "docker_container" "backend" {
  image = docker_image.backend.image_id
  name  = "hive-sampling-backend"
  ports {
    internal = 8080
    external = 8080
  }
  networks_advanced {
    name = docker_network.hive_sampling.name
  }
  healthcheck {
    test        = ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
    interval    = "10s"
    timeout     = "5s"
    retries     = 5
    start_period = "30s"
  }
  restart = "unless-stopped"
}

# Frontend Service (only for visualization)
resource "docker_image" "frontend" {
  name         = "ghcr.io/hive-sampling/frontend:latest"
  keep_locally = true
}

resource "docker_container" "frontend" {
  image = docker_image.frontend.image_id
  name  = "hive-sampling-frontend"
  ports {
    internal = 80
    external = 80
  }
  networks_advanced {
    name = docker_network.hive_sampling.name
  }
  depends_on = [docker_container.backend]
  restart    = "unless-stopped"
}
