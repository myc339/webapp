resource "aws_iam_policy" "CodeDeploy-EC2-S3" {
  name        = "CodeDeploy-EC2-S3"
  path        = "/"
  description = "CodeDeploy-EC2-S3 policy allows EC2 instances to read data from S3 buckets. This policy is required for EC2 instances to download latest application revision."

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "ec2:Describe*"
      ],
      "Effect": "Allow",
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_policy" "CircleCI-Upload-To-S3" {
  name        = "CircleCI-Upload-To-S3"
  path        = "/"
  description = "CircleCI-Upload-To-S3 policy allows CircleCI to upload artifacts from latest successful build to dedicated S3 bucket used by code deploy."

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:PutObject"
            ],
            "Resource": [
                "*"
            ]
        }
    ]
}
EOF
}

resource "aws_iam_policy" "circleci-ec2-ami" {
  name        = "circleci-ec2-ami"
  path        = "/"
  description = "circleci-ec2-ami policy allows CircleCI do something"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [{
      "Effect": "Allow",
      "Action" : [
        "ec2:AttachVolume",
        "ec2:AuthorizeSecurityGroupIngress",
        "ec2:CopyImage",
        "ec2:CreateImage",
        "ec2:CreateKeypair",
        "ec2:CreateSecurityGroup",
        "ec2:CreateSnapshot",
        "ec2:CreateTags",
        "ec2:CreateVolume",
        "ec2:DeleteKeyPair",
        "ec2:DeleteSecurityGroup",
        "ec2:DeleteSnapshot",
        "ec2:DeleteVolume",
        "ec2:DeregisterImage",
        "ec2:DescribeImageAttribute",
        "ec2:DescribeImages",
        "ec2:DescribeInstances",
        "ec2:DescribeInstanceStatus",
        "ec2:DescribeRegions",
        "ec2:DescribeSecurityGroups",
        "ec2:DescribeSnapshots",
        "ec2:DescribeSubnets",
        "ec2:DescribeTags",
        "ec2:DescribeVolumes",
        "ec2:DetachVolume",
        "ec2:GetPasswordData",
        "ec2:ModifyImageAttribute",
        "ec2:ModifyInstanceAttribute",
        "ec2:ModifySnapshotAttribute",
        "ec2:RegisterImage",
        "ec2:RunInstances",
        "ec2:StopInstances",
        "ec2:TerminateInstances"
      ],
      "Resource" : "*"
  }]
}
EOF
}

# resource "aws_iam_policy" "CircleCI-Code-Deploy" {
#   name        = "CircleCI-Code-Deploy"
#   path        = "/"
#   description = "CircleCI-Code-Deploy policy allows CircleCI to call CodeDeploy APIs to initiate application deployment on EC2 instances."

#   policy = <<EOF
# {
#   "Version": "2012-10-17",
#   "Statement": [
#     {
#       "Effect": "Allow",
#       "Action": [
#         "codedeploy:RegisterApplicationRevision",
#         "codedeploy:GetApplicationRevision"
#       ],
#       "Resource": [
#         "arn:aws:codedeploy:${var.region}:${var.account_id}:application:CODE_DEPLOY_APPLICATION_NAME"
#       ]
#     },
#     {
#       "Effect": "Allow",
#       "Action": [
#         "codedeploy:CreateDeployment",
#         "codedeploy:GetDeployment"
#       ],
#       "Resource": [
#         "*"
#       ]
#     },
#     {
#       "Effect": "Allow",
#       "Action": [
#         "codedeploy:GetDeploymentConfig"
#       ],
#       "Resource": [
#         "arn:aws:codedeploy:${var.region}:${var.account_id} :deploymentconfig:CodeDeployDefault.OneAtATime",
#         "arn:aws:codedeploy:${var.region}:${var.account_id} :deploymentconfig:CodeDeployDefault.HalfAtATime",
#         "arn:aws:codedeploy:${var.region}:${var.account_id} :deploymentconfig:CodeDeployDefault.AllAtOnce"
#       ]
#     }
#   ]
# }
# EOF
# }