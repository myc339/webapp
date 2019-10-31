# create RDS instance with Terraform
resource "aws_db_instance" "mydb1" {
    allocated_storage       = "${var.allocated_storage}"
	engine 					= "${var.engine}"
	instance_class 			= "${var.instance_class}"
	multi_az				= false
	identifier				= "csye6225-fall2019"
	username				= "dbuser"
	password				= "${var.password}"
	db_subnet_group_name	= "${var.db_subnet_group_name}"
	publicly_accessible		= true
	name 					= "csye6225"
	skip_final_snapshot     = true
	vpc_security_group_ids  =["${aws_security_group.rds_sg.id}"]
}