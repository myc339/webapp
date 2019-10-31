variable "password" {
  description 	= "Password for the master DB user. Note that this may show up in logs, and it will be stored in the state file"
  type        	= string
  default		= "csye6225!!!!"
}

variable "db_subnet_group_name" {
  type          = string
  default 		= ""
  description 	= "Apparently the group name, according to the RDS launch wizard."
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
