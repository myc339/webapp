filepath="file://csye6225-cf-networking.yml"
### user enter vpc's name
read  -p  "Enter Vpc name:" name
if [ -z "$name" ]
  then
    echo "enter your vpc name"
    exit 0
fi
## avoid check duplicate VPCS
vpcName="VPC $name"
name_response=$(aws ec2 describe-vpcs --filters Name=tag:Name,Values="$vpcName")
name_check=$(echo -e "$name_response"| jq '.Vpcs[0].VpcId'|tr -d '"')
if [ "$name_check" == "null" ]
  then
    echo "Continue Creating stack"
  else
    echo "VPC exists or AWS region error"
     exit 0
fi
## avoid duplicate stacks
## valid vpc cidr block
#
cidr_regex='^(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9]\.){3}[0-9]{1,3}(\/([0-9]|[1-2][0-9]|3[0-2]))?$'
read  -p  "Enter Vpc cidr block:" vpc_cidr_block
if [ -z "$vpc_cidr_block" ]
  then
    echo "enter  vpc cidr block"
    exit 0
fi
read  -p  "Enter subnet1 cidr block:" sub1_cidr_block
if [ -z "$sub1_cidr_block" ]
  then
    echo "enter  subnet1 cidr block"
    exit 0
fi
## valid subnet1 cidr block
read  -p  "Enter subnet2 cidr block:" sub2_cidr_block
if [ -z "$sub2_cidr_block" ]
  then
    echo "enter  subnet2 cidr block"
    exit 0
fi
## valid subnet1 cidr block
read  -p  "Enter subnet3 cidr block:" sub3_cidr_block
if [ -z "$sub3_cidr_block" ]
  then
    echo "enter  subnet3 cidr block"
    exit 0
fi
## valid subnets cidr block
stackId=$(aws cloudformation create-stack --stack-name "$name" --template-body "$filepath" --parameters \
ParameterKey=VpcCIDR,ParameterValue="$vpc_cidr_block" ParameterKey=PublicSubnet1CIDR,ParameterValue="$sub1_cidr_block" \
ParameterKey=PublicSubnet2CIDR,ParameterValue="$sub2_cidr_block" ParameterKey=PublicSubnet3CIDR,ParameterValue="$sub3_cidr_block")
aws cloudformation wait stack-create-complete --stack-name "$name"
aws cloudformation describe-stack-events --stack-name "$name" | jq -r  '.StackEvents[] | select(.ResourceStatus=="CREATE_FAILED" and .ResourceStatusReason!=null and .ResourceStatusReason!="Resource creation cancelled" ) .ResourceStatusReason'
#echo aws cloudformation describe-stack-events --stack-name "$name" | jq -r ".StackEvents[].ResourceStatusReason"



#aws cloudformation describe-stack-events --stack-name dsd | jq -r ".StackEvents[].ResourceStatusReason"