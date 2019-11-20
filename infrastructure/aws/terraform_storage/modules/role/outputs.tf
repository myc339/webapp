output "CodeDeployEC2ServiceRole" {
  value = "${aws_iam_role.CodeDeployEC2ServiceRole.name}"
}

output "CodeDeployServiceRole" {
  value = "${aws_iam_role.CodeDeployServiceRole.name}"
}

output "CodeDeployServiceRoleArn" {
  value = "${aws_iam_role.CodeDeployServiceRole.arn}"
}


output "LambdaServiceRole"{
  value="${aws_iam_role.LambdaServiceRole.name}"
}
output "LambdaServiceRoleArn"{
  value="${aws_iam_role.LambdaServiceRole.arn}"
}

output "role" {
  value = "${aws_iam_role.CodeDeployEC2ServiceRole}"
}