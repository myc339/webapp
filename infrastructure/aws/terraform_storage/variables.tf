variable "aws_access_key" {
  description = "The AWS access key"
	type = string
}

variable "aws_secret_key" {
  description = "The AWS secret key"
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

variable "account_id" {
    description = "aws account ID"
    type = string
}

variable "accessKey" {
  description = "The Circlci user access key"
  type = string
}

variable "secretKey" {
  description = "The Circlci user secret key"
  type = string
}

variable "dbName" {
  type = string
}

variable "dbUsername" {
  type = string
}

variable "dbPassword" {
  type = string
}

variable "HostzoneId" {
  type = string
}
variable "certificate" {
  type = string
}