mv /var/webapps/cloudwatch-config.json /opt
#sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
#    -a fetch-config \
#    -m ec2 \
#    -c file:/opt/cloudwatch-config.json \
#    -s