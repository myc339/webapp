resource "aws_iam_role_policy_attachment" "CodeDeployEC2ServiceRole_attachment" {
  role       = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "${var.CodeDeploy-EC2-S3}"
}

resource "aws_iam_role_policy_attachment" "CodeDeployServiceRole_attachment" {
  role       = "${var.CodeDeployServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSCodeDeployRole"
}
resource "aws_iam_role_policy_attachment" "CloudWatchAgentServerRole" {
  role = "${var.CloudWatchAgentServerRole}"
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy"
}