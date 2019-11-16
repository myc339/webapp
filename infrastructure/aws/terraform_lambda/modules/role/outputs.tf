
output "LambdaServiceRole"{
  value="${aws_iam_role.LambdaServiceRole.name}"
}
output "LambdaServiceRoleArn"{
  value="${aws_iam_role.LambdaServiceRole.arn}"
}