output "rds" {
  value = aws_db_instance.mydb
}

output "dbUrl" {
  value = aws_db_instance.mydb.endpoint
}
