package com.pucetec.roles.services

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.entities.Attendee
import com.pucetec.events.entities.Event
import com.pucetec.events.entities.Reservation
import com.pucetec.events.entities.ReservationStatus
import com.pucetec.events.exceptions.AttendeeNotFoundException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.ReservationAlreadyCancelledException
import com.pucetec.events.exceptions.ReservationLimitExceededException
import com.pucetec.events.exceptions.ReservationNotFoundException
import com.pucetec.events.exceptions.SoldOutException
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ReservationServiceTest {

    @Mock
    lateinit var reservationRepository: ReservationRepository

    @Mock
    lateinit var attendeeRepository: AttendeeRepository

    @Mock
    lateinit var eventRepository: EventRepository

    private lateinit var reservationService: ReservationService

    @BeforeEach
    fun setUp() {
        reservationService = ReservationService(reservationRepository, attendeeRepository, eventRepository)
    }

    @Test
    fun `should create reservation when data is valid`() {
        // Arrange
        val attendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val event = Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 10)
        val savedReservation = Reservation(
            id = 1L,
            attendee = attendee,
            event = event,
            status = ReservationStatus.ACTIVE,
            createdAt = LocalDateTime.now()
        )
        val request = ReservationRequest(attendeeId = 1L, eventId = 1L)

        whenever(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        whenever(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        whenever(reservationRepository.countByAttendeeIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(0L)
        whenever(eventRepository.save(any<Event>())).thenReturn(event)
        whenever(reservationRepository.save(any<Reservation>())).thenReturn(savedReservation)

        // Act
        val response = reservationService.createReservation(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals(ReservationStatus.ACTIVE, response.status)
    }

    @Test
    fun `should throw AttendeeNotFoundException when attendee does not exist`() {
        // Arrange
        val request = ReservationRequest(attendeeId = 99L, eventId = 1L)
        whenever(attendeeRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<AttendeeNotFoundException> { reservationService.createReservation(request) }
    }

    @Test
    fun `should throw EventNotFoundException when event does not exist`() {
        // Arrange
        val attendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val request = ReservationRequest(attendeeId = 1L, eventId = 99L)
        whenever(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        whenever(eventRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EventNotFoundException> { reservationService.createReservation(request) }
    }

    @Test
    fun `should throw SoldOutException when event has no available tickets`() {
        // Arrange
        val attendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val event = Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 0)
        val request = ReservationRequest(attendeeId = 1L, eventId = 1L)

        whenever(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        whenever(eventRepository.findById(1L)).thenReturn(Optional.of(event))

        // Act & Assert
        assertThrows<SoldOutException> { reservationService.createReservation(request) }
    }

    @Test
    fun `should throw ReservationLimitExceededException when attendee has 4 active reservations`() {
        // Arrange
        val attendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val event = Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 10)
        val request = ReservationRequest(attendeeId = 1L, eventId = 1L)

        whenever(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        whenever(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        whenever(reservationRepository.countByAttendeeIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(4L)

        // Act & Assert
        assertThrows<ReservationLimitExceededException> { reservationService.createReservation(request) }
    }

    @Test
    fun `should cancel reservation when it is active`() {
        // Arrange
        val attendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val event = Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 9)
        val reservation = Reservation(
            id = 1L,
            attendee = attendee,
            event = event,
            status = ReservationStatus.ACTIVE,
            createdAt = LocalDateTime.now()
        )

        whenever(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation))
        whenever(eventRepository.save(any<Event>())).thenReturn(event)
        whenever(reservationRepository.save(any<Reservation>())).thenReturn(reservation)

        // Act
        val response = reservationService.cancelReservation(1L)

        // Assert
        assertEquals(ReservationStatus.CANCELLED, response.status)
    }

    @Test
    fun `should throw ReservationNotFoundException when reservation does not exist`() {
        // Arrange
        whenever(reservationRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ReservationNotFoundException> { reservationService.cancelReservation(99L) }
    }

    @Test
    fun `should throw ReservationAlreadyCancelledException when reservation is already cancelled`() {
        // Arrange
        val attendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val event = Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 10)
        val reservation = Reservation(
            id = 1L,
            attendee = attendee,
            event = event,
            status = ReservationStatus.CANCELLED,
            createdAt = LocalDateTime.now()
        )

        whenever(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation))

        // Act & Assert
        assertThrows<ReservationAlreadyCancelledException> { reservationService.cancelReservation(1L) }
    }

    @Test
    fun `should return all reservations`() {
        // Arrange
        val attendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        val event = Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 10)
        val reservations = listOf(
            Reservation(id = 1L, attendee = attendee, event = event, status = ReservationStatus.ACTIVE, createdAt = LocalDateTime.now())
        )
        whenever(reservationRepository.findAll()).thenReturn(reservations)

        // Act
        val response = reservationService.getAllReservations()

        // Assert
        assertEquals(1, response.size)
    }
}
