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

resource "aws_iam_role_policy_attachment" "AmazonS3FullAccess_attachment" {
  role = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}
resource "aws_iam_role_policy_attachment" "S3AcessWithEncryption_attachment" {
  role = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "${var.S3AcessWithEncryption}"
}
resource "aws_iam_role_policy_attachment" "EC2_SNS_attachment" {
  role = "${var.CodeDeployEC2ServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/AmazonSNSFullAccess"
}

resource "aws_iam_role_policy_attachment" "LambdaBasicExecuteion_attachment" {
  role = "${var.LambdaServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}
resource "aws_iam_role_policy_attachment" "LambdaDynamodb_attachment" {
  role = "${var.LambdaServiceRole}"
  policy_arn = "${var.Lambda-DynamoDb}"
}