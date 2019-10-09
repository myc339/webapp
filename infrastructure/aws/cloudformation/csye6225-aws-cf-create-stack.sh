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
if [ "$name_check" == "null" ];
  then
    echo "Continue Creating"
  else
    echo "VPC exists"
     exit 0
fi
aws cloudformation describe-stacks --stack-name "$name"
aws cloudformation wait stack-exists
echo "Creating VPC..."
#stackId=$(aws cloudformation create-stack --stack-name "$name" --template-body "$filepath")
#aws cloudformation wait stack-create-complete --stack-name "$name"
#echo "Creating Stack..."
#if [ "$stackId" == "null" ];
#  then
#    echo "Stack create_failure,RollBack inProgress"
#  else
#    echo " Stack Create_Complete"
#     exit 0
#fi
#aws cloudformation update-stack --stack-name "$name"--template-body "$filepath"
#aws cloudformation wait stack-update-complete --stack-name "$name"
