package com.example.status.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestIdFilter : OncePerRequestFilter() {
    companion object {
        const val HEADER = "X-Request-ID"
        const val ATTRIBUTE = "requestId"
        private val SAFE_ID = Regex("[A-Za-z0-9._:-]{1,128}")
    }
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        // Bound untrusted IDs to prevent log injection; replace unsafe values.
        val requestId = request.getHeader(HEADER)?.takeIf { SAFE_ID.matches(it) } ?: UUID.randomUUID().toString()
        request.setAttribute(ATTRIBUTE, requestId)
        response.setHeader(HEADER, requestId)
        MDC.put(ATTRIBUTE, requestId)
        try { chain.doFilter(request, response) } finally { MDC.remove(ATTRIBUTE) }
    }
}
