package com.example.status.controller

import com.example.status.service.ServiceStatusService
import com.example.status.validation.StatusFilterValidator
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/services")
class ServiceStatusController(private val service: ServiceStatusService, private val validator: StatusFilterValidator, registry: MeterRegistry) {
    private val requests = registry.counter("service.status.dashboard.requests")

    @GetMapping
    fun getServices(@RequestParam(required = false) status: String?) = run {
        requests.increment()
        service.retrieve(validator.validate(status))
    }
}
