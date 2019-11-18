variable "launch_config" {
  type = string
}

variable "tg_arn" {
  type = string
}

variable "subnet_ids" {
    description = "Subnet IDs to associate with"
    type = list
}