resource "aws_launch_configuration" "asg_launch_config" {
  name          = "asg_launch_config"
  image_id      = "${var.ami}"
  instance_type = "t2.micro"
  associate_public_ip_address = true
  iam_instance_profile        = "${aws_iam_instance_profile.asgProfile.id}"

  # ebs_block_device {
  #   device_name = "/dev/sdh"
  # 	delete_on_termination = true
  # }

  root_block_device {
  	volume_size	= 20
  	volume_type = "gp2"
    delete_on_termination = true
  }

  key_name = "${var.key_pair_name}"
  security_groups = ["${var.vpc_security_group_id}"]
  depends_on = [var.depends_on_rds]
  
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
resource "aws_iam_instance_profile" "asgProfile" {
  name = "asgProfile"
  role = "${var.CodeDeployEC2ServiceRole}"
}