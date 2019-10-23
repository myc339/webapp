resource "aws_dynamodb_table" "csye6225" {
  name = "csye6225"
  hash_key = "id"
  write_capacity = "${var.write_capacity}"
  read_capacity = "${var.read_capacity}"
  attribute {
  	name = "id"
  	type = "S"
  }
}