variable "vpc_name" {
  description   = "The is VPC name"
  type          = string
  default       = "exclusiveVPC"
}
variable "all_subnet_cidr_block" {
  description   = "The is subnet address"
  type          = string
  default       = "10.0.1.0/24,10.0.2.0/24,10.0.3.0/24"
}
variable "vpc_cidr_block" {
  description   = "The is vpc address"
  type          = string
  default       = "10.0.0.0/16"
}