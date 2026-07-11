package com.pucetec.roles.dto

import com.pucetec.events.entities.ReservationStatus
import java.time.LocalDateTime

data class ReservationRequest(
    val attendeeId: Long,
    val eventId: Long
)

data class ReservationResponse(
    val id: Long,
    val attendeeId: Long,
    val attendeeName: String,
    val eventId: Long,
    val eventName: String,
    val status: ReservationStatus,
    val createdAt: LocalDateTime
)
