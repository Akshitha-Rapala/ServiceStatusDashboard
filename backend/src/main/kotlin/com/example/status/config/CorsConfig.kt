package com.example.status.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig(@Value("\${app.cors.allowed-origin}") private val allowedOrigin: String) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**").allowedOrigins(allowedOrigin).allowedMethods("GET")
            .allowedHeaders("Content-Type", RequestIdFilter.HEADER).exposedHeaders(RequestIdFilter.HEADER)
    }
}
