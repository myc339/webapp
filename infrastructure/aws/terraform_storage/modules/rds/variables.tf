variable "subnet_ids" {
  type          = list
  description 	= "Subnet ids from vpc"
}

variable "password" {
  description 	= "Password for the master DB user. Note that this may show up in logs, and it will be stored in the state file"
  type        	= string
}

variable "allocated_storage" {
  description   = "The allocated storage in gigabytes"
  type          = string
  default       = 20
}

variable "engine" {
  description   = "The database engine to use"
  type          = string
  default       = "mysql"
}

variable "instance_class" {
  description   = "The instance type of the RDS instance"
  type          = string
  default       = "db.t2.medium"
}

variable "rds_sg_id" {
  description   = "The RDS sg id"
  type          = string
}

variable "name" {
  type = string
}

variable "username" {
  type = string
}