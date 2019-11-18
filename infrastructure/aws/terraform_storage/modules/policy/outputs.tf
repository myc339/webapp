output "CodeDeploy-EC2-S3" {
  value = "${aws_iam_policy.CodeDeploy-EC2-S3.arn}"
}

output "CircleCI-Upload-To-S3" {
  value = "${aws_iam_policy.CircleCI-Upload-To-S3.arn}"
}
output "CircleCI-Update-LambdaFunctionCode" {
  value = "${aws_iam_policy.CircleCI-Update-LambdaFunctionCode.arn}"
}
output "CircleCI-Code-Deploy" {
  value = "${aws_iam_policy.CircleCI-Code-Deploy.arn}"
}

output "S3-Acess-With-Encryption" {
  value = "${aws_iam_policy.S3-Acess-With-Encryption.arn}"
}


output "Lambda-DynamoDb"{
  value="${aws_iam_policy.Lambda-DynamoDb.arn}"
}
output "Lambda-SES" {
  value = "${aws_iam_policy.Lambda-SES.arn}"
}