package com.pucetec.events.mappers

import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.entities.Reservation

fun Reservation.toResponse() = ReservationResponse(
    id = this.id,
    attendeeId = this.attendee.id,
    attendeeName = this.attendee.name,
    eventId = this.event.id,
    eventName = this.event.name,
    status = this.status,
    createdAt = this.createdAt
)
