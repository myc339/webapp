variable "ami" {
	description = "The ID of the AMI used to launch the instance"
	type = string
	default = "ami-0c97e61314cff2233"
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

variable "vpc_security_group_ids" {
    description = "Security group IDs to associate with"
    type = string
    default = "sg-b011c1e0"
}

