#!/bin/bash
#delete aws vpc
read  -p  "Enter Vpc name you wanna delete:" name
if [ -z "$name" ]
  then
    echo "enter Vpc name"
    exit 0
fi
vpcName="VPC $name"
Index="0 1 2"
name_response=$(aws ec2 describe-vpcs --filters Name=tag:Name,Values="$vpcName" )
VpcId_response=$(echo -e "$name_response"| jq '.Vpcs[0].VpcId'|tr -d '"')
echo "VPcId ="$VpcId_response
if [ "$VpcId_response" == "null" ];
  then
    echo "This Vpc not exsit"
    exit 0
  else
    echo "Start Delete"
fi

## delete subtnets
### find subnets' id
#SubnetId_response=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VpcId_response")
#SubnetIds=($(echo -e "$SubnetId_response" | jq '.Subnets[] | .SubnetId'| tr -d '"'))
## shellcheck disable=SC2154
#for i in $Index
#do
#  echo ${SubnetIds[$i]}
#  aws ec2 delete-subnet --subnet-id  "${SubnetIds[$i]}"
#done
#
#
#### deattach internet gateway to vpc
##### find internet gateway id
#internetgateway_response=$(aws ec2 describe-internet-gateways --filters Name=attachment.vpc-id,Values="$VpcId_response")
#internetgatewayId=$(echo -e "$internetgateway_response" | jq '.InternetGateways[0].InternetGatewayId'|tr -d '"')
#echo "gateway id="$internetgatewayId
#aws ec2 detach-internet-gateway --internet-gateway-id "$internetgatewayId" --vpc-id "$VpcId_response"
### delete internet gateway
#aws ec2 delete-internet-gateway --internet-gateway-id "$internetgatewayId"

#
### delete route table
#### find route table id
##importan must delete 10.0.0.0/24 then delete 0.0.0.0/0 
table_response=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$VpcId_response")
tableId=($(echo -e "$table_response" | jq '.RouteTables[].RouteTableId'|tr -d '"'))
for i in 1 0
do
echo "tableId="${tableId[$i]}
aws ec2 delete-route-table --route-table-id "${tableId[$i]}"
done
#
#
### delete vpc
aws ec2 delete-vpc --vpc-id "$VpcId_response"