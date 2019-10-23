provider "aws" {
  profile    = "${var.profile_name}"
  region     = "${var.aws_region}"
}

resource "aws_dynamodb_table" "csye6225" {
  name = "csye6225"
  hash_key = "id"
  write_capacity = "${var.write_capacity}"
  read_capacity = "${var.read_capacity}"
  attribute {
  	name = "id"
  	type = "S"
  }
}

#Create Security Group
resource "aws_security_group" "app_sg" {
  name        = "app_sg"
  description = "Allow TLS inbound traffic"
  vpc_id      = "vpc-0888f48abb0362064"

  ingress {
    # TLS (change to whatever ports you need)
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    # Please restrict your ingress to only necessary IPs and ports.
    # Opening to 0.0.0.0/0 can lead to security vulnerabilities.
    cidr_blocks     = ["0.0.0.0/0"]
  }
  ingress {
    # TLS (change to whatever ports you need)
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    # Please restrict your ingress to only necessary IPs and ports.
    # Opening to 0.0.0.0/0 can lead to security vulnerabilities.
    cidr_blocks     = ["0.0.0.0/0"]
  }
  ingress {
    # TLS (change to whatever ports you need)
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    # Please restrict your ingress to only necessary IPs and ports.
    # Opening to 0.0.0.0/0 can lead to security vulnerabilities.
    cidr_blocks     = ["0.0.0.0/0"]
  }
  ingress {
    # TLS (change to whatever ports you need)
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    # Please restrict your ingress to only necessary IPs and ports.
    # Opening to 0.0.0.0/0 can lead to security vulnerabilities.
    cidr_blocks     = ["0.0.0.0/0"]
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
  }

  tags = {
    Name = "app_sg"
  }
}

resource "aws_security_group" "rds_sg" {
  name = "rds_sg"
  description = "Allow TLS inbound traffic"
  vpc_id      = "vpc-0888f48abb0362064"

  ingress {
    # TLS (change to whatever ports you need)
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    # Please restrict your ingress to only necessary IPs and ports.
    # Opening to 0.0.0.0/0 can lead to security vulnerabilities.
    cidr_blocks     = ["0.0.0.0/0"]
    security_groups = ["${aws_security_group.app_sg.id}"]
  }

  tags = {
    Name = "rds_sg"
  }
}

# Enable Default Server Side Encryption
resource "aws_kms_key" "mykey" {
  description             = "This key is used to encrypt bucket objects"
  deletion_window_in_days = 10
}

# New resource for the S3 bucket our application will use.
resource "aws_s3_bucket" "myBucket" {
  # NOTE: S3 bucket names must be unique across _all_ AWS accounts, so
  # this name must be changed before applying this example to avoid naming
  # conflicts.
  bucket = "test.encrytion.lifcycle.wenkai.me"
  acl    = "private"

  # delete the bucket even if it is not empty
  force_destroy = true

  # Enable Default Server Side Encryption
  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        kms_master_key_id = "${aws_kms_key.mykey.arn}"
        sse_algorithm     = "aws:kms"
      }
    }
  }
  # Using object lifecycle
  lifecycle_rule {
    id      = "log"
    enabled = true

    prefix = "log/"

    tags = {
      "rule"      = "log"
      "autoclean" = "true"
    }

    transition {
      days          = 30
      storage_class = "STANDARD_IA" # or "ONEZONE_IA"
    }
  }
}

# Create subnet group
resource "aws_db_subnet_group" "dbsubnet" {
  name       = "dbsubnet"
  subnet_ids = var.db_subnets

  tags = {
    Name = "My DB subnet group"
  }
}

# create RDS instance with Terraform
resource "aws_db_instance" "mydb1" {
    allocated_storage       = "${var.allocated_storage}"
	engine 					= "${var.engine}"
	instance_class 			= "${var.instance_class}"
	multi_az				= false
	identifier				= "csye6225-fall2019"
	username				= "dbuser"
	password				= "${var.password}"
	db_subnet_group_name	= "${aws_db_subnet_group.dbsubnet.name}"
	publicly_accessible		= true
	name 					= "csye6225"
	skip_final_snapshot     = true
	vpc_security_group_ids  =["${aws_security_group.rds_sg.id}"]
}

# Create EC2
resource "aws_instance" "ec2-instance" {
  ami = "${var.ami}"
  instance_type = "${var.instance_type}"

  ebs_block_device {
    device_name = "/dev/sda1"
  	volume_size	= "${var.volume_size}"
  	volume_type = "${var.volume_type}"
  	delete_on_termination = true
  }

  disable_api_termination = false
  vpc_security_group_ids = ["${aws_security_group.app_sg.id}"]
  subnet_id = "${element(var.db_subnets, 0)}"

  depends_on = [aws_db_instance.mydb1]

  tags = {
    Name = "csye6225-ec2-instance"
  }
}