package com.example.status.model

import java.time.Instant

data class DashboardResponse(val services: List<ServiceInfo>, val summary: StatusSummary, val generatedAt: Instant)
