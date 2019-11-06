
sudo chmod 666 /var/webapps/assignment2/src/main/resources/application.properties
sudo cat /var/tmp/user_data.txt >> /var/webapps/assignment2/src/main/resources/application.properties
cd /var/webapps/assignment2
sudo mvn spring-boot:run
#

