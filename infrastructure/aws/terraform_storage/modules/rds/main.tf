# Create subnet group
resource "aws_db_subnet_group" "dbsubnet" {
  name       = "dbsubnet"
  subnet_ids = "${var.subnet_ids}"

  tags = {
    Name = "My DB subnet group"
  }
}

# create RDS instance with Terraform
resource "aws_db_instance" "mydb" {
    allocated_storage       = "${var.allocated_storage}"
	engine 					= "${var.engine}"
	instance_class 			= "${var.instance_class}"
	multi_az				= false
	identifier				= "csye6225-fall2019"
	username				= "${var.username}"
	password				= "${var.password}"
	db_subnet_group_name	= "${aws_db_subnet_group.dbsubnet.name}"
	publicly_accessible		= true
	name 					= "${var.name}"
	skip_final_snapshot     = true
	vpc_security_group_ids  =["${var.rds_sg_id}"]
}