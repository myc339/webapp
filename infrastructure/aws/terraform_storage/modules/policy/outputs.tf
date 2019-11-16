output "CodeDeploy-EC2-S3" {
  value = "${aws_iam_policy.CodeDeploy-EC2-S3.arn}"
}

output "CircleCI-Upload-To-S3" {
  value = "${aws_iam_policy.CircleCI-Upload-To-S3.arn}"
}

output "CircleCI-Code-Deploy" {
  value = "${aws_iam_policy.CircleCI-Code-Deploy.arn}"
}

output "S3-Acess-With-Encryption" {
  value = "${aws_iam_policy.S3-Acess-With-Encryption.arn}"
}