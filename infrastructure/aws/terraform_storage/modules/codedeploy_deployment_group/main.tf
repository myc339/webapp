resource "aws_codedeploy_deployment_group" "csye6225-webapp-deployment" {
  
  app_name              = "${var.appName}"
  # 1、Deployment group name - csye6225-webapp-deployment
  deployment_group_name = "csye6225-webapp-deployment"
  # 2、Service role - CodeDeployServiceRole
  service_role_arn      = "${var.CodeDeployServiceRoleArn}"
  # 3、Deployment type - In-place
  deployment_style {
    deployment_option = "WITHOUT_TRAFFIC_CONTROL"
    deployment_type   = "IN_PLACE"
  }
  # 4、Environment Configuration - Amazon EC2 Instances. Provide the tag group key and values.
  ec2_tag_set {
    ec2_tag_filter {
      key   = "Name"
      type  = "KEY_AND_VALUE"
      value = "csye6225-ec2-instance"
    }
  }
  # 5、Deployment settings - CodeDeployDefault.AllAtOnce
  deployment_config_name = "CodeDeployDefault.AllAtOnce"
  # 6、Load Balancer - disabled
  load_balancer_info{
  }
  # 7、Rollback - Roll back when a deployment fails
  auto_rollback_configuration {
    enabled = true
    events  = ["DEPLOYMENT_FAILURE"]
  }

  trigger_configuration {
    trigger_events     = ["DeploymentFailure"]
    trigger_name       = "example-trigger"
    trigger_target_arn = "${aws_sns_topic.example.arn}"
  }
}

resource "aws_sns_topic" "example" {
  name = "example-topic"
}

# profile depends on CodeDeployEC2ServiceRole
resource "aws_iam_instance_profile" "test_profile" {
  name = "test_profile"
  role = "CodeDeployEC2ServiceRole"
}