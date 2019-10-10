provider "aws" {
  profile    = "${var.profile_name}"
  region     = "${var.aws_region}"
}
module "vpcs" {
  source="./module/network"
  vpc_name="${var.vpc_name}A"
  all_subnet_cidr_block="${var.all_subnet_cidr_block}"
  vpc_cidr_block="${var.vpc_cidr_block}"
  //  username="ddee"
}
module "vpcs2" {

  source="./module/network"
  vpc_name="${var.vpc_name}B"
  all_subnet_cidr_block="${var.all_subnet_cidr_block}"
  vpc_cidr_block="${var.vpc_cidr_block}"
  //  username="ddee"
}