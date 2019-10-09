### user enter vpc's name
read  -p  "Enter Vpc name which you wanna delete:" name
if [ -z "$name" ]
  then
    echo "enter vpc name"
    exit 0
fi
## check stack exist
check_stack=$(aws cloudformation describe-stacks --stack-name "$name")
if [ -z "$check_stack" ]
  then
    echo "stack "$name" don't exist"
    exit 0
  else
    echo "$check_stack"|jq '.Stacks[0].StackId'
fi
#aws cloudformation delete-stack --stack-name myteststack
#aws cloudformation wait stack-delete-complete --stack-name "$name"