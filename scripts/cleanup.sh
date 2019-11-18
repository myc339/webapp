sudo mv /opt/tomcat/webapps/assignment2-0.0.1-SNAPSHOT.war /opt/tomcat/webapps/ROOT.war
sudo rm /opt/tomcat/webapps/ROOT.war
#sudo rm -rf /opt/tomcat/webapps/app
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -m ec2 -a stop
sudo systemctl stop tomcat
#sudo rm -rf /opt/tomcat/webapps/ROOT