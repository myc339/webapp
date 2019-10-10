### user enter vpc's name
read  -p  "Enter stack name which you wanna delete:" name
if [ -z "$name" ]
  then
    echo "enter stack name"
    exit 0
fi
## check stack exist
check=$(aws cloudformation describe-stacks --stack-name "$name")
aws cloudformation delete-stack --stack-name "$name"
aws cloudformation wait stack-delete-complete --stack-name "$name"

