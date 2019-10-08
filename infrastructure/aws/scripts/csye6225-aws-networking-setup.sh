#!/bin/bash
# create-aws-vpc

### user enter vpc's name
read  -p  "Enter Vpc name:" name
if [ -z "$name" ]
  then
    echo "enter your vpc name"
    exit 0
fi
#variables used in script:
vpcName="VPC $name"
SubnetIndex="0 1 2"
subnetName=("$name Subnet1" "$name Subnet2" "$name Subnet3")
availabilityZone=("us-east-1a" "us-east-1b" "us-east-1c")
subNetCidrBlock=("10.0.1.0/24" "10.0.2.0/24" "10.0.4.0/24")
gatewayName="$name Gateway"
routeTableName="$name Route Table"
vpcCidrBlock="10.0.0.0/16"
destinationCidrBlock="0.0.0.0/0"
## check user is exist or not
name_response=$(aws ec2 describe-vpcs --filters Name=tag:Name,Values="$vpcName" )
name_check=$(echo -e "$name_response"| jq '.Vpcs[0].VpcId'|tr -d '"')
# shellcheck disable=SC2170
if [ "$name_check" == "null" ];
  then
    echo " null "
  else
    echo " not null"
     exit 0
fi
echo "Creating VPC..."
#create vpc with cidr block /16
aws_response=$(aws ec2 create-vpc --cidr-block "$vpcCidrBlock" )
vpcId=$(echo -e "$aws_response"| jq '.Vpc.VpcId'| tr -d '"')
if [ -z "$vpcId" ]
then
    exit 0
fi
##name the vpc
aws ec2 create-tags --resources "$vpcId" --tags Key=Name,Value="$vpcName"


for i in $SubnetIndex
do
  #create subnet1 for vpc with /24 cidr block
  subnet_response=$(aws ec2 create-subnet --cidr-block "${subNetCidrBlock[$i]}"  --availability-zone "${availabilityZone[$i]}" --vpc-id "$vpcId" )
  subnetId[$i]=$(echo -e "$subnet_response" |  jq '.Subnet.SubnetId' | tr -d '"')
  if [ -z "${subnetId[$i]}" ]
then
    exit 0
fi
  #name the subtnet1
  aws ec2 create-tags --resources "${subnetId[$i]}" --tags Key=Name,Value="${subnetName[$i]}"
done

#create internet gateway
gateway_response=$(aws ec2 create-internet-gateway)
gatewayId=$(echo -e "$gateway_response" |  jq '.InternetGateway.InternetGatewayId' | tr -d '"')
if [ -z "$gatewayId" ]
then
    exit 0
fi
#name the internet gateway
aws ec2 create-tags --resources "$gatewayId" --tags Key=Name,Value="$gatewayName"


#attach gateway to vpc
attach_response=$(aws ec2 attach-internet-gateway --internet-gateway-id "$gatewayId"  --vpc-id "$vpcId")
## check attach success or not
if  [ ! -z "$attach_response" ]
then
    exit 0
fi
#create route table for vpc
route_table_response=$(aws ec2 create-route-table --vpc-id "$vpcId")
routeTableId=$(echo -e "$route_table_response" |  jq '.RouteTable.RouteTableId' | tr -d '"')
if [ -z "$routeTableId" ]
then
    exit 0
fi

#name the route table
aws ec2 create-tags --resources "$routeTableId" --tags Key=Name,Value="$routeTableName"
#add route for the internet gateway
route_response=$(aws ec2 create-route --route-table-id "$routeTableId" --destination-cidr-block "$destinationCidrBlock" --gateway-id "$gatewayId")
if [ "$route_response" ]
then
    exit 0
fi
#add route to subnet
for i in $SubentIndex
do
associate_response=$(aws ec2 associate-route-table --subnet-id "${subnetId[$i]}" --route-table-id "$routeTableId")
if [ -z "$associate_response" ]
then
    exit 0
fi
done
echo "Vpc Created Successfully"
# end of create-aws-vpc