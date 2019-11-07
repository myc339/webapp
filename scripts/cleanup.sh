sudo rm /opt/tomcat/webapps/assignment2-0.0.1-SNAPSHOT.war
sudo rm -rf /opt/tomcat/webapps/assignment2-0.0.1-SNAPSHOT
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -m ec2 -a stop
sudo systemctl stop tomcat