output "circleci-ec2-ami" {
  value = "${aws_iam_policy.circleci-ec2-ami.arn}"
}