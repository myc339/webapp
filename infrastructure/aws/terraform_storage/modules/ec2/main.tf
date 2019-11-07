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
/bin/sudo touch /var/tmp/user_data.txt
/bin/echo "" > /var/tmp/user_data.txt
/bin/echo "region=${var.region}" >> /var/tmp/user_data.txt
/bin/echo "bucketName=${var.bucketName}" >>/var/tmp/user_data.txt
/bin/echo "dbUrl=${var.dbUrl}" >> /var/tmp/user_data.txt
/bin/echo "dbName=${var.dbName}" >>/var/tmp/user_data.txt
/bin/echo "spring.datasource.username=${var.dbUsername}" >> /var/tmp/user_data.txt
/bin/echo "spring.datasource.password=${var.dbPassword}" >>/var/tmp/user_data.txt

####################################################
# TOMCAT SHOULD BE INSTALLED WHEN BUILDING THE AMI #
####################################################

cd /etc/systemd/system/
echo '[Unit]' > tomcat.service
echo 'Description=Apache Tomcat Web Application Container' >> tomcat.service
echo 'After=syslog.target network.target' >> tomcat.service
echo '[Service]' >> tomcat.service
echo 'Type=forking' >> tomcat.service
echo 'Environment=JAVA_HOME=/usr/bin/java' >> tomcat.service
echo 'Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid' >> tomcat.service
echo 'Environment=CATALINA_HOME=/opt/tomcat' >> tomcat.service
echo 'Environment=CATALINA_BASE=/opt/tomcat' >> tomcat.service
echo 'Environment="CATALINA_OPTS=-Xms256M -Xmx256M -server -XX:+UseParallelGC"' >> tomcat.service
echo 'Environment="JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"' >> tomcat.service
echo 'ExecStart=/opt/tomcat/bin/startup.sh' >> tomcat.service
echo 'ExecStop=/opt/tomcat/bin/shutdown.sh' >> tomcat.service
echo 'User=tomcat' >> tomcat.service
echo 'Group=tomcat' >> tomcat.service
echo 'UMask=0007' >> tomcat.service
echo 'RestartSec=10' >> tomcat.service
echo 'Restart=always' >> tomcat.service
echo '[Install]' >> tomcat.service
echo 'WantedBy=multi-user.target' >> tomcat.service
# sudo systemctl daemon-reload
# sudo systemctl enable tomcat
# sudo systemctl start tomcat
sudo ufw allow 8080
####################################################
# Configure Tomcat JAVA_OPTS                       #
####################################################
cd /opt/tomcat/bin
touch setenv.sh
echo "#!/bin/sh" > setenv.sh
echo "JAVA_OPTS=\"\$JAVA_OPTS -Dspring.datasource.username=${var.dbUsername} -Dspring.datasource.password=${var.dbPassword} -DdbUrl=${var.dbUrl} -DdbName=${var.dbName} -DbucketName=${var.bucketName} -Dregion=${var.region}\"" >> setenv.sh
chown tomcat:tomcat setenv.sh
chmod +x setenv.sh
sudo chmod 755 -R /opt/tomcat
# Start Tomcat
/bin/bash /opt/tomcat/bin/catalina.sh start
--//
EOF
}

# profile depends on CodeDeployEC2ServiceRole
resource "aws_iam_instance_profile" "profile" {
  name = "profile"
  role = "${var.CodeDeployEC2ServiceRole}"
}