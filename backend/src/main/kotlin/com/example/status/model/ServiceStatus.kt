package com.example.status.model

enum class ServiceStatus { OPERATIONAL, DEGRADED, DOWN }

data class ServiceInfo(val id: String, val name: String, val description: String, val status: ServiceStatus)
