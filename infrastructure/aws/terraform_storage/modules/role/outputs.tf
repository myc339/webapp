output "CodeDeployEC2ServiceRole" {
  value = "${aws_iam_role.CodeDeployEC2ServiceRole.name}"
}

output "CodeDeployServiceRole" {
  value = "${aws_iam_role.CodeDeployServiceRole.name}"
}