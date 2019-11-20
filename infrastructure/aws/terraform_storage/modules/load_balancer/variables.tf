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
//  default = "Z2OBD6XZ6VFH6R"
}

variable "domain_name" {
  type = string
//  default = "dev.myc339.me"
}

variable "certificate" {
  type = string
//  default = "arn:aws:acm:us-east-1:589079856728:certificate/cac51dbb-1c88-4905-92be-7591f346bc56"
}
