variable "domain_name" {
  description   = "The is your domain name"
  type          = string
}

variable "account_id" {
    description = "aws account ID"
    type = string
}

variable "CodeDeployEC2ServiceRole" {
  type = string
}

variable "depends_on_role" {
  type			= any
  default		= null
}