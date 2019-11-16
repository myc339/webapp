provider "aws" {
  profile    = "${var.profile_name}"
  region     = "${var.aws_region}"
}



# Create policies
module "policy" {
  source = "./modules/policy"
}

# Create roles
module "role" {
  source = "./modules/role"
}

# Attach policy to role
module "role_policy_attachment" {
  source = "./modules/role_policy_attachment"
  LambdaServiceRole="${module.role.LambdaServiceRole}"
  Lambda-DynamoDb="${module.policy.Lambda-DynamoDb}"
}


#create lambda
module "lambda" {
  source = "./modules/lambda"
  LambdaServiceRole="${module.role.LambdaServiceRoleArn}"
}