# This data source is included for ease of sample architecture deployment
# and can be swapped out as necessary.
data "aws_availability_zones" "available" {}

resource "aws_vpc" "vpc" {
  enable_dns_support   = true
  enable_dns_hostnames = true
  cidr_block = "${var.vpc_cidr_block}"
  tags = {
    Name = "${var.vpc_name}"
  }
}

resource "aws_subnet" "subnet" {
  count = 3

  availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
  cidr_block        = "${element(split(",", "${var.all_subnet_cidr_block}"), count.index)}"
  vpc_id            = "${aws_vpc.vpc.id}"

  tags = {
    Name = "${var.vpc_name}+${count.index}"
  }
}



resource "aws_internet_gateway" "gw" {
  vpc_id = "${aws_vpc.vpc.id}"

  tags = {
    Name = "${var.vpc_name}+internet_gateway"
  }
}

resource "aws_route_table" "route_table" {
  count = 3
  vpc_id = "${aws_vpc.vpc.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.gw.id}"
  }

  tags = {
    Name = "${var.vpc_name}+route_table"
  }
}

resource "aws_route_table_association" "route_table_asso" {
  count = 3

  subnet_id      = "${element(aws_subnet.subnet.*.id, count.index)}"
  route_table_id = "${element(aws_route_table.route_table.*.id, count.index)}"
}