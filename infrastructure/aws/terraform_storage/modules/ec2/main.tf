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
  vpc_security_group_ids = ["${var.vpc_security_group_id}"]
  subnet_id = "${element(var.subnet_ids, 0)}"
  key_name = "${var.key_pair_name}"

  depends_on = [var.depends_on_rds]

  tags = {
    Name = "csye6225-ec2-instance"
  }

  associate_public_ip_address = true
  iam_instance_profile = "${aws_iam_instance_profile.profile.id}"
  user_data = <<EOF
Content-Type: multipart/mixed; boundary="//"
MIME-Version: 1.0

--//
Content-Type: text/cloud-config; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="cloud-config.txt"

#cloud-config
cloud_final_modules:
- [scripts-user, always]

--//
Content-Type: text/x-shellscript; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="userdata.txt"

#!/bin/bash
####################################################
# Configure Tomcat JAVA_OPTS                       #
####################################################
cd /opt/tomcat/bin
touch setenv.sh
echo "#!/bin/sh" > setenv.sh
echo "JAVA_OPTS=\"\$JAVA_OPTS -Dspring.datasource.username=${var.dbUsername} -Dspring.datasource.password=${var.dbPassword} -DdbUrl=${var.dbUrl} -DdbName=${var.dbName} -DbucketName=webapp.${var.bucketName} -Dregion=${var.region}  \"" >> setenv.sh
chown tomcat:tomcat setenv.sh
chmod +x setenv.sh
sudo chmod 755 -R /opt/tomcat
# Start Tomcat
--//
EOF
}

# profile depends on CodeDeployEC2ServiceRole
resource "aws_iam_instance_profile" "profile" {
  name = "profile"
  role = "${var.CodeDeployEC2ServiceRole}"
}