package com.example.status.error

class InvalidStatusException : RuntimeException("Status must be one of: OPERATIONAL, DEGRADED, DOWN")
