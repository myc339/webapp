resource "aws_lb" "applb" {
  name               = "applb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = ["${var.sg_id}"]
  subnets            = "${var.subnet_ids}"
#   access_logs {
#     bucket  = "${aws_s3_bucket.lb_logs.bucket}"
#     prefix  = "test-lb"
#     enabled = true
#   }

  tags = {
    Environment = "production"
  }
}

resource "aws_lb_listener" "listener" {
  load_balancer_arn = "${aws_lb.applb.arn}"
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = "${var.certificate}"

  default_action {
    type             = "forward"
    target_group_arn = "${aws_lb_target_group.targetGroup.arn}"
  }
}

resource "aws_lb_target_group" "targetGroup" {
  name        = "targetGroup"
  target_type = "instance"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = "${var.vpc_id}"
}

resource "aws_route53_record" "www" {
  zone_id = "${var.zone_id}"
  name    = "${var.domain_name}"
  type    = "A"
#   records = ["${aws_lb.applb.dns_name}"]
  alias {
    name                   = "${aws_lb.applb.dns_name}"
    zone_id                = "${aws_lb.applb.zone_id}"
    evaluate_target_health = true
  }
}

resource "aws_wafregional_web_acl_association" "wafAssociation" {
  resource_arn = "${aws_lb.applb.arn}"
  web_acl_id   = "${var.web_acl_id}"
}