# # Change the aws_instance we declared earlier to now include "depends_on"
# resource "aws_instance" "example" {
#   ami           = "ami-2757f631"
#   instance_type = "t2.micro"

#   # Tells Terraform that this EC2 instance must be created only after the
#   # S3 bucket has been created.
#   depends_on = [aws_s3_bucket.example]
# }

# Enable Default Server Side Encryption
resource "aws_kms_key" "mykey" {
  description             = "This key is used to encrypt bucket objects"
  deletion_window_in_days = 10
}

# New resource for the S3 bucket our application will use.
resource "aws_s3_bucket" "myBucket" {
  # NOTE: S3 bucket names must be unique across _all_ AWS accounts, so
  # this name must be changed before applying this example to avoid naming
  # conflicts.
  bucket = "test.encrytion.lifcycle.wenkai.me"
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

