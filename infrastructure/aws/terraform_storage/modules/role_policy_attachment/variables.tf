# Role name
variable "CodeDeployEC2ServiceRole" {
  type = string
}

variable "CodeDeployServiceRole" {
  type = string
}

# Policy arn
variable "CodeDeploy-EC2-S3" {
  type = string
}

variable "CloudWatchAgentServerRole"{
  type =string
}

