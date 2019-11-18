resource "aws_autoscaling_group" "asg" {
  name                      = "asg"

  default_cooldown          = 60
  max_size                  = 10
  min_size                  = 3
  desired_capacity          = 3
  force_delete              = true
  launch_configuration      = "${var.launch_config}"
  vpc_zone_identifier       = "${var.subnet_ids}"

  tag {
    key                 = "Name"
    value               = "csye6225-ec2-instance"
    propagate_at_launch = true
  }

  target_group_arns         = ["${var.tg_arn}"]
}