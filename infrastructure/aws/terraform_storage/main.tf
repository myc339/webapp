provider "aws" {
  region     = "${var.aws_region}"
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
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
  account_id = "${var.account_id}"
}
# Create lambda bucket


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
# module "ec2" {
#   source = "./modules/ec2"
#   depends_on_rds = [module.rds.rds]
#   vpc_security_group_id = "${module.security_group.app_sg_id}"
#   subnet_ids = "${module.vpc.subnet_ids}"
#   key_pair_name = "${var.key_pair_name}"
#   ami = "${var.ami}"
#   CodeDeployEC2ServiceRole = "${module.role.CodeDeployEC2ServiceRole}"
#   # application params
#   region = "${var.aws_region}"
#   dbUrl = "${module.rds.dbUrl}"
#   dbPassword = "${var.dbPassword}"
#   bucketName="${var.domain_name}"
#   dbName = "${var.dbName}"
#   dbUsername = "${var.dbUsername}"
#   aws_secret_key="${var.aws_secret_key}"
#   aws_access_key="${var.aws_access_key}"
#   snsArn = "${module.lambda.snsArn}"
# }

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
  LambdaServiceRole="${module.role.LambdaServiceRole}"

  CodeDeploy-EC2-S3 = "${module.policy.CodeDeploy-EC2-S3}"
  S3AcessWithEncryption = "${module.policy.S3-Acess-With-Encryption}"
  Lambda-DynamoDb="${module.policy.Lambda-DynamoDb}"
  Lambda-SES = "${module.policy.Lambda-SES}"
}

# Attach policy to user
module "user_policy_attachment" {
  source = "./modules/user_policy_attachment"
  CircleCI-Upload-To-S3 = "${module.policy.CircleCI-Upload-To-S3}"
  CircleCI-Code-Deploy = "${module.policy.CircleCI-Code-Deploy}"
  CircleCI-Update-LambdaFunctionCode = "${module.policy.CircleCI-Update-LambdaFunctionCode}"
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

# Create launch configuration
module "launch_config" {
  source = "./modules/launch_config"
  depends_on_rds = [module.rds.rds]
  vpc_security_group_id = "${module.security_group.app_sg_id}"
  key_pair_name = "${var.key_pair_name}"
  ami = "${var.ami}"
  CodeDeployEC2ServiceRole = "${module.role.CodeDeployEC2ServiceRole}"
  # application params
  region = "${var.aws_region}"
  dbUrl = "${module.rds.dbUrl}"
  dbPassword = "${var.dbPassword}"
  bucketName="${var.domain_name}"
  dbName = "${var.dbName}"
  dbUsername = "${var.dbUsername}"
  aws_secret_key="${var.aws_secret_key}"
  aws_access_key="${var.aws_access_key}"
  snsArn = "${module.lambda.snsArn}"
}

# Create auto scaling group
module "auto_scaling_group" {
  source = "./modules/auto_scaling_group"
  launch_config = "${module.launch_config.launch_config_name}"
  tg_arn = "${module.load_balancer.tg_arn}"
  subnet_ids = "${module.vpc.subnet_ids}"
}

# Create auto scaling policy
module "autoscaling_policy" {
  source = "./modules/autoscaling_policy"
  asg_name = "${module.auto_scaling_group.asg_name}"
}

# Create auto load balancer
module "load_balancer" {
  source = "./modules/load_balancer"
  asg_arn = "${module.auto_scaling_group.asg_arn}"
  web_acl_id = "${module.waf.wafWebACL}"
  subnet_ids = "${module.vpc.subnet_ids}"
  vpc_id = "${module.vpc.vpc_id}"
  sg_id = "${module.security_group.app_sg_id}"
}

# Create waf
module "waf" {
  source = "./modules/waf"
}

#create lambda
module "lambda" {
  source = "./modules/lambda"
  LambdaServiceRole="${module.role.LambdaServiceRoleArn}"
  dynamodbName = "${module.dynamodb.dynamodbName}"
  region = "${var.aws_region}"
}