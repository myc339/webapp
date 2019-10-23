provider "aws" {
  profile    = "dev"
  region     = "us-east-1"
}

module "dynamodb" {
  source="./module/dynamodb"
}

module "rds" {
  source="./module/rds"
}

module "ec2" {
  source="./module/ec2"
}

module "s3_bucket" {
  source="./module/s3_bucket"
}

module "security_group" {
  source="./module/security_group"
}