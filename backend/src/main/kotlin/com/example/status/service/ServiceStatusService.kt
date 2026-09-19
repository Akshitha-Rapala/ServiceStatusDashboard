package com.example.status.service

import com.example.status.model.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ServiceStatusService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val services = listOf(
        ServiceInfo("authentication", "Authentication", "Identity, sign-in, and session management.", ServiceStatus.OPERATIONAL),
        ServiceInfo("payments", "Payments", "Payment processing and billing operations.", ServiceStatus.OPERATIONAL),
        ServiceInfo("notifications", "Notifications", "Email, push, and in-app message delivery.", ServiceStatus.DEGRADED),
        ServiceInfo("search", "Search", "Indexing and search across the platform.", ServiceStatus.OPERATIONAL),
        ServiceInfo("ai", "AI", "AI-powered inference and platform assistance.", ServiceStatus.DOWN)
    )
    fun retrieve(status: ServiceStatus?): DashboardResponse {
        val filtered = services.filter { status == null || it.status == status }
        logger.info("event=service_status_retrieved filter={} returned={} total={}", status ?: "ALL", filtered.size, services.size)
        return DashboardResponse(filtered, StatusSummary(services.size,
            services.count { it.status == ServiceStatus.OPERATIONAL },
            services.count { it.status == ServiceStatus.DEGRADED },
            services.count { it.status == ServiceStatus.DOWN }), Instant.now())
    }
}
