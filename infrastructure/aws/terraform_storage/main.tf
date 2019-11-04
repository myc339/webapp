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
  name = "${var.dbName}"
  username = "${var.dbUsername}"
  password = "${var.dbPassword}"
}

# Create EC2
module "ec2" {
  source = "./modules/ec2"
  depends_on_rds = [module.rds.rds]
  vpc_security_group_id = "${module.security_group.app_sg_id}"
  subnet_ids = "${module.vpc.subnet_ids}"
  key_pair_name = "${var.key_pair_name}"
  ami = "${var.ami}"
  CodeDeployEC2ServiceRole = "${module.role.CodeDeployEC2ServiceRole}"
  # application params
  region = "${var.aws_region}"
  accessKey = "${var.accessKey}"
  secretKey = "${var.secretKey}"
  dbUrl = "${module.rds.dbUrl}"
  dbPassword = "${var.dbPassword}"
  bucketName = "${module.s3_bucket.bucketName}"
  dbName = "${var.dbName}"
  dbUsername = "${var.dbUsername}"
}

# Create policies
module "policy" {
  source = "./modules/policy"
  region = "${var.aws_region}"
  account_id = "${var.account_id}"
  code_dp_name = "${module.codedeploy_app.name}"
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
  CircleCI-Code-Deploy = "${module.policy.CircleCI-Code-Deploy}"
  circleci-ec2-ami = "${module.policy.circleci-ec2-ami}"

}

# Create codedeploy application
module "codedeploy_app" {
  source = "./modules/codedeploy_app"
  
}

# Create codedeploy development group
module "codedeploy_development_group" {
  source = "./modules/codedeploy_deployment_group"
  appName = "${module.codedeploy_app.name}"
  CodeDeployServiceRoleArn = "${module.role.CodeDeployServiceRoleArn}"
}