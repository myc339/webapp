data "archive_file" "init"{
  type="zip"
  output_path = "${path.module}/demo.zip"
  source {
    content = "hello world"
    filename = "init.txt"
  }
}
resource "aws_sns_topic" "sns" {
  name = "email_request"
}
resource "aws_lambda_function" "lambda" {
  filename = "${data.archive_file.init.output_path}"
  function_name = "csye6225_lambda"
  role          = "${var.LambdaServiceRole}"
  handler = "sns::handleRequest"
  # The filebase64sha256() function is available in Terraform 0.11.12 and later
  # For Terraform 0.11.11 and earlier, use the base64sha256() function and the file() function:
  # source_code_hash = "${base64sha256(file("lambda_function_payload.zip"))}"
  //  source_code_hash = "${filebase64sha256("lambda_function_payload.zip")}"
  runtime = "java8"
  reserved_concurrent_executions = 1
  timeout = 15*60
  memory_size = 512
    environment {
      variables = {
        tableName = "${var.dynamodbName}"
        region = "${var.region}"
        system_mail = "${aws_ses_domain_mail_from.example.mail_from_domain}"
        ttl = "30"
      }
    }
}
resource "aws_sns_topic_subscription" "lambda" {
  topic_arn = "${aws_sns_topic.sns.arn}"
  protocol  = "lambda"
  endpoint  = "${aws_lambda_function.lambda.arn}"
}
resource "aws_lambda_permission" "with_sns" {
  statement_id = "AllowExecutionFromSNS"
  action = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.lambda.arn}"
  principal = "sns.amazonaws.com"
  source_arn = "${aws_sns_topic.sns.arn}"
}

## ses main send
resource "aws_ses_domain_mail_from" "example" {
  domain           = "${var.domainName}"
  mail_from_domain = "noreply@${var.domainName}"
}

# Example Route53 MX record
resource "aws_route53_record" "example_ses_domain_mail_from_mx" {
  zone_id = "${var.zoneId}"
  name    = "${aws_ses_domain_mail_from.example.mail_from_domain}"
  type    = "MX"
  ttl     = "600"
  records = ["10 feedback-smtp.us-east-1.amazonses.com"]           # Change to the region in which `aws_ses_domain_identity.example` is created
}

# Example Route53 TXT record for SPF
resource "aws_route53_record" "example_ses_domain_mail_from_txt" {
  zone_id = "${var.zoneId}"
  name    = "${aws_ses_domain_mail_from.example.mail_from_domain}"
  type    = "TXT"
  ttl     = "600"
  records = ["v=spf1 include:amazonses.com -all"]
}
