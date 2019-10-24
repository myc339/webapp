variable "profile_name" {
    description = "The AWS profile name"
	type = string
}
variable "aws_region" {
    description = "The region of the resource"
	type = string
}
variable "write_capacity" {
  description = "The number of write units for this table. If the billing_mode is PROVISIONED, this field is required."
  type        = number
  default     = 1
}

variable "read_capacity" {
  description = "The number of read units for this table. If the billing_mode is PROVISIONED, this field is required."
  type        = number
  default     = 1
}
variable "ami" {
	description = "The ID of the AMI used to launch the instance"
	type = string
}

variable "instance_type" {
	description = "The type of the Instance"
	type = string
	default = "t2.micro"
}

variable "volume_size" {
	description = "The size of the volume, in GiB"
	default = 20
}

variable "volume_type" {
	description = "The volume type"
	type = string
	default = "gp2"
}

variable "password" {
  description 	= "Password for the master DB user. Note that this may show up in logs, and it will be stored in the state file"
  type        	= string
  default		= "csye6225!!!!"
}

# variable "db_subnets" {
#   type          = list
#   default 		= ["subnet-013178f8e46e46dad","subnet-0dbb7d21b12c7e7c5"]
#   description 	= "Apparently the group name, according to the RDS launch wizard."
# }

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

variable "domain_name" {
  description   = "The is your domain name"
  type          = string
}

variable "key_pair_name" {
  description   = "The is your public key name in AWS"
  type          = string
}