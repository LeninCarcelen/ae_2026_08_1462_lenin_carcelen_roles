package com.pucetec.events.services

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.InvalidCapacityException
import com.pucetec.events.mappers.toEntity
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.EventRepository
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository
) {

    fun createEvent(request: EventRequest): EventResponse {
        if (request.name.isBlank() || request.venue.isBlank()) {
            throw BlankFieldException("El nombre y el lugar del evento son obligatorios")
        }

        if (request.totalTickets < 1) {
            throw InvalidCapacityException("La capacidad total debe ser al menos 1")
        }

        val event = request.toEntity()
        val persistedEvent = eventRepository.save(event)
        return (persistedEvent ?: event).toResponse()
    }

    fun getAllEvents(): List<EventResponse> {
        return eventRepository.findAll().map { it.toResponse() }
    }

    fun getEventById(id: Long): EventResponse {
        val event = eventRepository.findById(id)
            .orElseThrow { EventNotFoundException("Evento con id $id no encontrado") }
        return event.toResponse()
    }
}
