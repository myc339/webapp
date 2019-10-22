variable "password" {
  description 	= "Password for the master DB user. Note that this may show up in logs, and it will be stored in the state file"
  type        	= string
  default		= "csye6225!!!!"
}

variable "db_subnet_group_name" {
  default 		= "vpc-0561adb2a0f6a0266"
  description 	= "Apparently the group name, according to the RDS launch wizard."
}

