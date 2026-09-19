package com.example.status.error

import java.time.Instant

data class ApiError(val timestamp: Instant, val status: Int, val error: String, val message: String, val path: String, val requestId: String)
