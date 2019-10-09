# This data source is included for ease of sample architecture deployment
# and can be swapped out as necessary.
data "aws_availability_zones" "available" {}

resource "aws_vpc" "demo" {
  cidr_block = "${var.vpc_cidr_block}"
  tags = {
    Name = "${var.vpc_name}"
  }
}

# variable "name" {
  
# }


resource "aws_subnet" "demo" {
  count = 3

  availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
  cidr_block        = "${element(split(",", "${var.all_subnet_cidr_block}"), count.index)}"
  vpc_id            = "${aws_vpc.demo.id}"

  tags = {
    Name = "${var.vpc_name}+${count.index}"
  }


resource "aws_internet_gateway" "demo1" {
  vpc_id = "${aws_vpc.demo1.id}"
}

resource "aws_route_table" "demo1" {
  vpc_id = "${aws_vpc.demo1.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.demo1.id}"
  }
}

resource "aws_route_table_association" "demo2" {
  count = 3

  subnet_id      = "${aws_subnet.demo1.*.id[count.index]}"
  route_table_id = "${aws_route_table.demo1.id}"
}

provider "aws" {
<<<<<<< HEAD
  profile    = "${var.profile_name}"
  region = "${var.aws_region}"
=======
  region = "us-east-1"
  profile    = "dev"
>>>>>>> yumeng/assignment4
}