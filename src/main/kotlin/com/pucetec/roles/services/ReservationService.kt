package com.pucetec.roles.services

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.entities.Reservation
import com.pucetec.events.entities.ReservationStatus
import com.pucetec.events.exceptions.AttendeeNotFoundException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.ReservationAlreadyCancelledException
import com.pucetec.events.exceptions.ReservationLimitExceededException
import com.pucetec.events.exceptions.ReservationNotFoundException
import com.pucetec.events.exceptions.SoldOutException
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val attendeeRepository: AttendeeRepository,
    private val eventRepository: EventRepository
) {

    companion object {
        const val MAX_ACTIVE_RESERVATIONS = 4
    }

    fun createReservation(request: ReservationRequest): ReservationResponse {
        val attendee = attendeeRepository.findById(request.attendeeId)
            .orElseThrow { AttendeeNotFoundException("Asistente con id ${request.attendeeId} no encontrado") }

        val event = eventRepository.findById(request.eventId)
            .orElseThrow { EventNotFoundException("Evento con id ${request.eventId} no encontrado") }

        if (event.availableTickets <= 0) {
            throw SoldOutException("El evento ${event.name} no tiene entradas disponibles")
        }

        val activeReservations = reservationRepository.countByAttendeeIdAndStatus(
            attendee.id,
            ReservationStatus.ACTIVE
        )

        if (activeReservations >= MAX_ACTIVE_RESERVATIONS) {
            throw ReservationLimitExceededException(
                "El asistente ${attendee.name} ya tiene el máximo de $MAX_ACTIVE_RESERVATIONS reservas activas"
            )
        }

        event.availableTickets -= 1
        eventRepository.save(event)

        val reservation = Reservation(
            attendee = attendee,
            event = event,
            status = ReservationStatus.ACTIVE,
            createdAt = LocalDateTime.now()
        )

        val savedReservation = reservationRepository.save(reservation)
        return (savedReservation ?: reservation).toResponse()
    }

    fun cancelReservation(reservationId: Long): ReservationResponse {
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { ReservationNotFoundException("Reserva con id $reservationId no encontrada") }

        if (reservation.status == ReservationStatus.CANCELLED) {
            throw ReservationAlreadyCancelledException("La reserva con id $reservationId ya está cancelada")
        }

        reservation.status = ReservationStatus.CANCELLED

        val event = reservation.event
        event.availableTickets += 1
        eventRepository.save(event)

        val savedReservation = reservationRepository.save(reservation)
        return (savedReservation ?: reservation).toResponse()
    }

    fun getAllReservations(): List<ReservationResponse> {
        return reservationRepository.findAll().map { it.toResponse() }
    }
}
