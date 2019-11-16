provider "aws" {
  region     = "${var.aws_region}"
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
}

# Create policies
module "policy" {
  source = "./modules/policy"
  region = "${var.aws_region}"
  account_id = "${var.account_id}"
}

# Attach policy to user
module "user_policy_attachment" {
  source = "./modules/user_policy_attachment"
  circleci-ec2-ami = "${module.policy.circleci-ec2-ami}"
}