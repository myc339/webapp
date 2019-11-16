resource "aws_iam_policy" "Lambda-DynamoDb" {
  name        = "Lambda-DynamoDb"
  path        = "/"
  description = "allow lambda put and read items in dynamodb"

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "dynamodb:PutItem",
                "dynamodb:GetItem",
                "dynamodb:GetRecords"
            ],
            "Resource": "*"
        }
    ]
}
EOF
}

