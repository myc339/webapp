resource "aws_autoscaling_policy" "WebServerScaleUpPolicy" {
  name                   = "WebServerScaleUpPolicy"
  policy_type            = "SimpleScaling"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 60
  autoscaling_group_name = "${var.asg_name}"
}

resource "aws_autoscaling_policy" "WebServerScaleDownPolicy" {
  name                   = "WebServerScaleDownPolicy"
  policy_type            = "SimpleScaling"
  scaling_adjustment     = -1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 60
  autoscaling_group_name = "${var.asg_name}"
}

resource "aws_cloudwatch_metric_alarm" "CPUAlarmHigh" {
  alarm_name          = "CPUAlarmHigh"
  alarm_description   = "Scale-up if CPU > 90% for 10 minutes"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  statistic           = "Average"
  period              = "300"
  evaluation_periods  = "2"
  threshold           = "90"
  alarm_actions     = ["${aws_autoscaling_policy.WebServerScaleUpPolicy.arn}"]

  dimensions = {
    AutoScalingGroupName = "${var.asg_name}"
  }

  comparison_operator = "GreaterThanThreshold"
}

resource "aws_cloudwatch_metric_alarm" "CPUAlarmLow" {
  alarm_name          = "CPUAlarmLow"
  alarm_description   = "Scale-down if CPU < 70% for 10 minutes"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  statistic           = "Average"
  period              = "300"
  evaluation_periods  = "2"
  threshold           = "70"
  alarm_actions     = ["${aws_autoscaling_policy.WebServerScaleDownPolicy.arn}"]

  dimensions = {
    AutoScalingGroupName = "${var.asg_name}"
  }

  comparison_operator = "LessThanThreshold"
}