#!/bin/bash
# create-aws-vpc

### user enter vpc's name
read  -p  "Enter Vpc name:" name
if [ -z "$name" ]
  then
    echo "enter vpc name"
    exit 0
fi
#variables used in script:
vpcName="VPC $name"
SubnetIndex="0 1 2"
subnetName=("$name Subnet1" "$name Subnet2" "$name Subnet3")
gatewayName="$name Gateway"
routeTableName="$name Route Table"
destinationCidrBlock="0.0.0.0/0"
## check user is exist or not
name_response=$(aws ec2 describe-vpcs --filters Name=tag:Name,Values="$vpcName" )
name_check=$(echo -e "$name_response"| jq '.Vpcs[0].VpcId'|tr -d '"')
# shellcheck disable=SC2170
if [ "$name_check" == "null" ];
  then
    echo " continue "

  else
    echo " VPc exists or AWS region error"
    exit 0
fi
echo "Creating VPC..."

### user enter  vpc cidr block

read  -p  "enter VPC CIDR block:" vpcCidrBlock
if [ -z "$vpcCidrBlock" ]
  then
    echo "enter VPC CIDR block"
    exit 0
fi
#create vpc with cidr block /16
aws_response=$(aws ec2 create-vpc --cidr-block "$vpcCidrBlock" )
vpcId=$(echo -e "$aws_response"| jq '.Vpc.VpcId'| tr -d '"')
if [ -z "$vpcId" ]
then
    exit 0
fi
##name the vpc
aws ec2 create-tags --resources "$vpcId" --tags Key=Name,Value="$vpcName"


### user enter subnet cidr block 1
#### get  availability-zones
zones=$(aws ec2 describe-availability-zones)
availabilityZone=($(echo -e "$zones" | jq '.AvailabilityZones[].ZoneName' | tr -d '"'))
echo "create 3 subnets"
for i in $SubnetIndex
do
  #create subnet1 for vpc with /24 cidr block
  read  -p  "enter VPC subnet CIDR block:" subnetCidrBlock
if [ -z "$subnetCidrBlock" ]
  then
    echo "enter subnet CIDR block"
    exit 0
fi
  subnet_response=$(aws ec2 create-subnet --cidr-block "$subnetCidrBlock"  --availability-zone "${availabilityZone[$i]}" --vpc-id "$vpcId" )
  subnetId[$i]=$(echo -e "$subnet_response" |  jq '.Subnet.SubnetId' | tr -d '"')
  # shellcheck disable=SC2077
  if [ -z "${subnetId[$i]}" ] || [ "${subnetId[$i]}" == "null" ]
  then
    echo 'subnet cidr block error'
    exit 0
  fi
  #name the subtnet1
  aws ec2 create-tags --resources "${subnetId[$i]}" --tags Key=Name,Value="${subnetName[$i]}"
done



#create internet gateway
gateway_response=$(aws ec2 create-internet-gateway)
gatewayId=$(echo -e "$gateway_response" |  jq '.InternetGateway.InternetGatewayId' | tr -d '"')
#name the internet gateway
aws ec2 create-tags --resources "$gatewayId" --tags Key=Name,Value="$gatewayName"
echo "create internet gateway"

#attach gateway to vpc
attach_response=$(aws ec2 attach-internet-gateway --internet-gateway-id "$gatewayId"  --vpc-id "$vpcId")
## check attach success or not
echo "attach gateway to vpc"

#create route table for vpc
route_table_response=$(aws ec2 create-route-table --vpc-id "$vpcId")
routeTableId=$(echo -e "$route_table_response" |  jq '.RouteTable.RouteTableId' | tr -d '"')
if [ -z "$routeTableId" ]
then
    exit 0
fi
echo "1 $routeTableId"
#name the route table
aws ec2 create-tags --resources "$routeTableId" --tags Key=Name,Value="$routeTableName"
echo "create routetable"
#add route for the internet gateway
route_response=$(aws ec2 create-route --route-table-id "$routeTableId" --destination-cidr-block "$destinationCidrBlock" --gateway-id "$gatewayId")
echo "attach gateway to route-table"
#add route to subnet
echo "2 $routeTableId"
for i in $SubentIndex
do
associate_response=$(aws ec2 associate-route-table --subnet-id "${subnetId[$i]}" --route-table-id "$routeTableId")
done
echo "attach subnets to routetable"
echo "Vpc Created Successfully"
# end of create-aws-vpc