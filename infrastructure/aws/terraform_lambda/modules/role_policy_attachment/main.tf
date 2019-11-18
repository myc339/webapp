resource "aws_iam_role_policy_attachment" "LambdaBasicExecuteion_attachment" {
  role = "${var.LambdaServiceRole}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}
resource "aws_iam_role_policy_attachment" "LambdaDynamodb_attachment" {
  role = "${var.LambdaServiceRole}"
  policy_arn = "${var.Lambda-DynamoDb}"
}