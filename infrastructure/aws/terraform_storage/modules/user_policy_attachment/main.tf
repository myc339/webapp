resource "aws_iam_user_policy_attachment" "user_attach1" {
  user       = "circleci"
  policy_arn = "${var.CircleCI-Upload-To-S3}"
}

resource "aws_iam_user_policy_attachment" "user_attach2" {
  user       = "circleci"
  policy_arn = "${var.CircleCI-Code-Deploy}"
}

resource "aws_iam_user_policy_attachment" "circleci_lambda" {
  policy_arn = "${var.CircleCI-Update-LambdaFunctionCode}"
  user = "circleci"
}