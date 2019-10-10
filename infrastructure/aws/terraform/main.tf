provider "aws" {
  profile    = "${var.profile_name}"
  region     = "${var.aws_region}"
}
module "vpcs" {
  source="./module/network"
  vpc_name="${var.vpc_name}"
  all_subnet_cidr_block="${var.all_subnet_cidr_block}"
  vpc_cidr_block="${var.vpc_cidr_block}"
}