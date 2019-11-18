output "launch_config_name" {
  value = "${aws_launch_configuration.asg_launch_config.name}"
}
