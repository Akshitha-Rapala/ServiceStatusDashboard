package com.example.status

import com.example.status.config.RequestIdFilter
import com.example.status.controller.ServiceStatusController
import com.example.status.error.GlobalExceptionHandler
import com.example.status.service.ServiceStatusService
import com.example.status.validation.StatusFilterValidator
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

class ServiceStatusControllerTest {
    private val registry = SimpleMeterRegistry()
    private val mvc = MockMvcBuilders.standaloneSetup(ServiceStatusController(ServiceStatusService(), StatusFilterValidator(), registry))
        .setControllerAdvice(GlobalExceptionHandler()).addFilters<StandaloneMockMvcBuilder>(RequestIdFilter()).build()

    @Test fun `filter changes list but preserves fleet summary`() {
        mvc.perform(get("/api/v1/services").param("status", "down").header("X-Request-ID", "test-123"))
            .andExpect(status().isOk).andExpect(jsonPath("$.services.length()").value(1))
            .andExpect(jsonPath("$.services[0].name").value("AI"))
            .andExpect(jsonPath("$.summary.total").value(5))
            .andExpect(jsonPath("$.summary.operational").value(3))
            .andExpect(header().string("X-Request-ID", "test-123"))
        assertNull(MDC.get("requestId"))
    }
    @Test fun `invalid filter returns correlated error and increments metric`() {
        mvc.perform(get("/api/v1/services").param("status", "broken").header("X-Request-ID", "error-123"))
            .andExpect(status().isBadRequest).andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.requestId").value("error-123"))
            .andExpect(jsonPath("$.path").value("/api/v1/services"))
            .andExpect(jsonPath("$.message").value("Status must be one of: OPERATIONAL, DEGRADED, DOWN"))
        assertEquals(1.0, registry.get("service.status.dashboard.requests").counter().count())
        assertNull(MDC.get("requestId"))
    }
    @Test fun `unfiltered request returns five services and generates request ID`() {
        val result = mvc.perform(get("/api/v1/services"))
            .andExpect(status().isOk).andExpect(jsonPath("$.services.length()").value(5))
            .andExpect(jsonPath("$.generatedAt").exists()).andReturn()
        assertNotNull(java.util.UUID.fromString(result.response.getHeader("X-Request-ID")))
    }
    @Test fun `unsafe request IDs are replaced`() {
        val result = mvc.perform(get("/api/v1/services").header("X-Request-ID", "x".repeat(129)))
            .andExpect(status().isOk).andReturn()
        assertNotNull(java.util.UUID.fromString(result.response.getHeader("X-Request-ID")))
    }
}
