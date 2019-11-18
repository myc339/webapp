output "wafWebACL" {
  value = "${aws_cloudformation_stack.waf.outputs["wafWebACL"]}"
}
