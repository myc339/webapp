resource "aws_iam_user_policy_attachment" "user_attach1" {
  user       = "circleci"
  policy_arn = "${var.CircleCI-Upload-To-S3}"
}

resource "aws_iam_user_policy_attachment" "user_attach2" {
  user       = "circleci"
  policy_arn = "${var.CircleCI-Code-Deploy}"
}