package com.example.status

import com.example.status.model.ServiceStatus
import com.example.status.service.ServiceStatusService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ServiceStatusServiceTest {
    private val service = ServiceStatusService()
    @Test fun `each filter returns only matching services`() {
        val expected = mapOf(ServiceStatus.OPERATIONAL to 3, ServiceStatus.DEGRADED to 1, ServiceStatus.DOWN to 1)
        expected.forEach { (status, count) ->
            val response = service.retrieve(status)
            assertEquals(count, response.services.size)
            assertTrue(response.services.all { it.status == status })
            assertEquals(5, response.summary.total)
        }
    }
}
