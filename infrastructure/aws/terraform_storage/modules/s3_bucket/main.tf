# Enable Default Server Side Encryption
resource "aws_kms_key" "mykey" {
  description             = "This key is used to encrypt bucket objects"
  deletion_window_in_days = 10
  policy = <<POLICY
{
    "Version": "2012-10-17",
    "Id": "key-default-1",
    "Statement": [
        {
            "Sid": "Enable IAM User Permissions",
            "Effect": "Allow",
            "Principal": {
                "AWS": [
                    "arn:aws:iam::${var.account_id}:role/${var.CodeDeployEC2ServiceRole}",
                    "arn:aws:iam::${var.account_id}:root"
                ]
            },
            "Action": "kms:*",
            "Resource": "*"
        }
    ]
}
POLICY
}

# New resource for the S3 bucket our application will use to store images.
resource "aws_s3_bucket" "imageBucket" {
  # NOTE: S3 bucket names must be unique across _all_ AWS accounts, so
  # this name must be changed before applying this example to avoid naming
  # conflicts.
  bucket = "webapp.${var.domain_name}"
  acl    = "private"

  # delete the bucket even if it is not empty
  force_destroy = true

  # Enable Default Server Side Encryption
  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        kms_master_key_id = "${aws_kms_key.mykey.arn}"
        sse_algorithm     = "aws:kms"
      }
    }
  }
  # Using object lifecycle
  lifecycle_rule {
    id      = "log"
    enabled = true

    prefix = "log/"

    tags = {
      "rule"      = "log"
      "autoclean" = "true"
    }

    transition {
      days          = 30
      storage_class = "STANDARD_IA" # or "ONEZONE_IA"
    }
  }
}

resource "aws_s3_bucket_public_access_block" "imageBucketBlock" {
  bucket = "${aws_s3_bucket.imageBucket.id}"
  ignore_public_acls=true
  block_public_acls   = true
  block_public_policy = true
  restrict_public_buckets=true
}

# Create the S3 bucket for CodeDeploy.
resource "aws_s3_bucket" "codeDeployBucket" {
  # NOTE: S3 bucket names must be unique across _all_ AWS accounts, so
  # this name must be changed before applying this example to avoid naming
  # conflicts.
  bucket = "codedeploy.${var.domain_name}"
  acl    = "private"

  # delete the bucket even if it is not empty
  force_destroy = true

  # Enable Default Server Side Encryption
  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        kms_master_key_id = "${aws_kms_key.mykey.arn}"
        sse_algorithm     = "aws:kms"
      }
    }
  }
  # Using object lifecycle
  lifecycle_rule {
    id      = "log"
    enabled = true

    prefix = "log/"

    tags = {
      "rule"      = "log"
      "autoclean" = "true"
    }

    expiration {
      days = 60
    }
  }
}

resource "aws_s3_bucket_public_access_block" "codeDeployBucketBlock" {
  bucket = "${aws_s3_bucket.codeDeployBucket.id}"
  ignore_public_acls=true
  block_public_acls   = true
  block_public_policy = true
  restrict_public_buckets=true
}