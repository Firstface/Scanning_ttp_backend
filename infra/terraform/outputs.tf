output "backend_url" {
  description = "Backend service URL"
  value       = "http://localhost:8080"
}

output "frontend_url" {
  description = "Frontend service URL"
  value       = "http://localhost:80"
}

output "network_name" {
  description = "Docker network name"
  value       = docker_network.hive_sampling.name
}
