variable "write_capacity" {
  description = "The number of write units for this table. If the billing_mode is PROVISIONED, this field is required."
  type        = number
  default     = 1
}

variable "read_capacity" {
  description = "The number of read units for this table. If the billing_mode is PROVISIONED, this field is required."
  type        = number
  default     = 1
}
