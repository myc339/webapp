resource "aws_cloudformation_stack" "waf" {
  name         = "waf-stack"
  template_url = "https://s3.amazonaws.com/myc339.me.owasp10/owasp_10.yml"
}
