resource "aws_iam_role_policy_attachment" "CodeDeployEC2ServiceRole_attachment" {
  role       = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "${var.CodeDeploy-EC2-S3}"
}

resource "aws_iam_role_policy_attachment" "CodeDeployServiceRole_attachment" {
  role       = "${var.CodeDeployServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSCodeDeployRole"
}
resource "aws_iam_role_policy_attachment" "CloudWatchAgentServerRole_attachment" {
  role = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy"
}
<<<<<<< HEAD
//resource "aws_iam_role_policy_attachment" "AmazonRDSFullAccess_attachment" {
//  role = "${var.CodeDeployEC2ServiceRole}"
//  policy_arn = "arn:aws:iam::aws:policy/AmazonRDSFullAccess"
//}
=======
resource "aws_iam_role_policy_attachment" "AmazonS3FullAccess_attachment" {
  role = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}
resource "aws_iam_role_policy_attachment" "S3AcessWithEncryption_attachment" {
  role = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "${var.S3AcessWithEncryption}"
}
>>>>>>> dev
