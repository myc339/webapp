variable "subnet_ids" {
  type = list
}

variable "asg_arn" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "web_acl_id" {
  type = string
}

variable "sg_id" {
  type = string
}

variable "zone_id" {
  type = string
  default = "Z3COERZ0L1NRIP"
}

variable "domain_name" {
  type = string
  default = "dev.wenkai.me"
}

variable "certificate" {
  type = string
  default = "arn:aws:acm:us-east-1:403196973460:certificate/c6a1db07-57a2-4313-a485-31ea7cf33edb"
}
