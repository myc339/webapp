variable "profile_name" {
    description = "The AWS profile name"
	type = string
}
variable "aws_region" {
    description = "The region of the resource"
	type = string
}

variable "ami" {
	description = "The ID of the AMI used to launch the instance"
	type = string
}

variable "key_pair_name" {
  description   = "The is your public key name in AWS"
  type          = string
}

variable "domain_name" {
  description   = "The is your domain name"
  type          = string
}

# variable "account_id" {
#     description = "aws account ID"
#     type = string
# }