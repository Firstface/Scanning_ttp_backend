variable "image_tag" {
  description = "Docker image tag"
  type        = string
  default     = "latest"
}

variable "backend_port" {
  description = "Backend container port"
  type        = number
  default     = 8080
}

variable "frontend_port" {
  description = "Frontend container port"
  type        = number
  default     = 80
}
