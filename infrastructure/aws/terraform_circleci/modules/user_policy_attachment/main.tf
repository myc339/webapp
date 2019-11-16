resource "aws_iam_user_policy_attachment" "user_attach_ami" {
  user       = "circleci"
  policy_arn = "${var.circleci-ec2-ami}"
}