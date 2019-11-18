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

variable "S3AcessWithEncryption"{
  type = string
}


# Role name

variable "LambdaServiceRole"{
  type = string
}
# Policy arn

variable "Lambda-DynamoDb" {
  type = string
}

variable "Lambda-SES" {
  type = string
}