output "imageBucketName" {
  value = aws_s3_bucket.imageBucket.id
}

output "codeDeployBucketName" {
  value = aws_s3_bucket.codeDeployBucket.id
}