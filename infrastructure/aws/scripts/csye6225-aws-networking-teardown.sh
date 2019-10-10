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

# delete subtnets
## find subnets' id
SubnetId_response=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VpcId_response")
SubnetIds=($(echo -e "$SubnetId_response" | jq '.Subnets[] | .SubnetId'| tr -d '"'))
echo "delete subnets"
## if subnets created failed or partially succeed
## shellcheck disable=SC2154
for ((i=0;i<${#SubnetIds[@]};i++))
  do
   aws ec2 delete-subnet --subnet-id  "${SubnetIds[$i]}"
  done


### deattach internet gateway to vpc
#### find internet gateway id
internetgateway_response=$(aws ec2 describe-internet-gateways --filters Name=attachment.vpc-id,Values="$VpcId_response")
internetgatewayId=$(echo -e "$internetgateway_response" | jq '.InternetGateways[0].InternetGatewayId'|tr -d '"')
if [ "$internetgatewayId" == "null" ];
  then
    echo "internetgateway not exists "
  else
    echo "deattach gateway  then delete  "
    aws ec2 detach-internet-gateway --internet-gateway-id "$internetgatewayId" --vpc-id "$VpcId_response"
    ## delete internet gateway
    aws ec2 delete-internet-gateway --internet-gateway-id "$internetgatewayId"
fi


### delete route table
#### find route table id
##importan must delete 10.0.0.0/24 then delete 0.0.0.0/0
echo "delete route table"
table_response=$(aws ec2 describe-route-tables --filters Name=vpc-id,Values="$VpcId_response")
maintableId=$(echo -e "$table_response" | jq '.RouteTables[]| select(.Associations[]!=null) .RouteTableId ' |tr -d '"')
tableId=($(echo -e "$table_response" | jq '.RouteTables[].RouteTableId' |tr -d '"'))
len=${#tableId[@]}
for ((i=0;i<$len;i++))
do
  if ! [ "$maintableId" == "${tableId[$i]}" ]
    then
      echo "equal $i"
      subtableId=${tableId[$i]}
  fi
done
if ! [ -z "$subtableId" ]
then aws ec2 delete-route-table --route-table-id "$subtableId"
fi
#aws ec2 disassociate-route-table --association-id "${CidrBlocks[0]}"
#aws ec2 delete-route --route-table-id  "$maintableId" --destination-cidr-block "${CidrBlocks[0]}"
#aws ec2 delete-route-table --route-table-id "$maintableId"
#
#
#### delete vpc
#echo "delete vpc"
aws ec2 delete-vpc --vpc-id "$VpcId_response"