provider "aws" {
	region = "us-east-1"
}

resource "aws_instance" "ec2-instance" {
  ami = var.ami
  instance_type = var.instance_type

  ebs_block_device {
    device_name = "/dev/sda1"
  	volume_size	= var.volume_size
  	volume_type = var.volume_type
  	delete_on_termination = true
  }

  disable_api_termination = false
  vpc_security_group_ids = [var.vpc_security_group_ids]

  tags = {
    Name = "csye6225-ec2-instance"
  }
}