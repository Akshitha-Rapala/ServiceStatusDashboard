package com.example.status.model

/** Fleet-wide counts remain stable when the service list is filtered. */
data class StatusSummary(val total: Int, val operational: Int, val degraded: Int, val down: Int)
