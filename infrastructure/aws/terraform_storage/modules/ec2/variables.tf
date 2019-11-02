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

variable "vpc_security_group_id" {
    description = "Security group ID to associate with"
    type = string
}

variable "subnet_ids" {
    description = "Subnet IDs to associate with"
    type = list
}

variable "key_pair_name" {
  description   = "The is your public key name in AWS"
  type          = string
}

variable "depends_on_rds" {
  type			= any
  default		= null
}
