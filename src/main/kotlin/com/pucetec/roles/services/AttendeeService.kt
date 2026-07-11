package com.pucetec.roles.services

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.dto.AttendeeResponse
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.mappers.toEntity
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.AttendeeRepository
import org.springframework.stereotype.Service

@Service
class AttendeeService(
    private val attendeeRepository: AttendeeRepository
) {

    fun createAttendee(request: AttendeeRequest): AttendeeResponse {
        if (request.name.isBlank() || request.email.isBlank()) {
            throw BlankFieldException("El nombre y el email del asistente son obligatorios")
        }

        val attendee = request.toEntity()
        val persistedAttendee = attendeeRepository.save(attendee)
        return (persistedAttendee ?: attendee).toResponse()
    }
}
