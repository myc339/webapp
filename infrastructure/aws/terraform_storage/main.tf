provider "aws" {
  profile    = "${var.profile_name}"
  region     = "${var.aws_region}"
}

# Create VPC
module "vpc" {
  source = "./modules/vpc"
}

# Create DynamoDB
module "dynamodb" {
  source = "./modules/dynamodb"
}

# Create Security Group
module "security_group" {
  source = "./modules/security_group"
  vpc_id      = "${module.vpc.vpc_id}"
}

# Create S3 bucket
module "s3_bucket" {
  source = "./modules/s3_bucket"
  domain_name = "${var.domain_name}"
}

# Create RDS
module "rds" {
  source = "./modules/rds"
  subnet_ids = "${module.vpc.subnet_ids}"
  rds_sg_id = "${module.security_group.rds_sg_id}"
}

# Create EC2
module "ec2" {
  source = "./modules/ec2"
  depends_on_rds = [module.rds.rds]
  vpc_security_group_id = "${module.security_group.app_sg_id}"
  subnet_ids = "${module.vpc.subnet_ids}"
  key_pair_name = "${var.key_pair_name}"
  ami = "${var.ami}"
}

# Create policies
module "policy" {
  source = "./modules/policy"
  region = "${var.region}"
  # account_id = "${var.account_id}"
}

# Create roles
module "role" {
  source = "./modules/role"
}

# Attach policy to role
module "role_policy_attachment" {
  source = "./modules/role_policy_attachment"
  CodeDeployEC2ServiceRole = "${module.role.CodeDeployEC2ServiceRole}"
  CodeDeployServiceRole = "${module.role.CodeDeployServiceRole}"
  CodeDeploy-EC2-S3 = "${module.policy.CodeDeploy-EC2-S3}"
}

# Attach policy to user
module "user_policy_attachment" {
  source = "./modules/user_policy_attachment"
  CircleCI-Upload-To-S3 = "${module.policy.CircleCI-Upload-To-S3}"
  # CircleCI-Code-Deploy = "${module.policy.CircleCI-Code-Deploy}"
  circleci-ec2-ami = "${module.policy.circleci-ec2-ami}"

}