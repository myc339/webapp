resource "aws_cloudformation_stack" "waf" {
  name         = "waf-stack"
  template_url = "https://s3.us-east-2.amazonaws.com/awswaf-owasp/owasp_10_base.yml"
}
