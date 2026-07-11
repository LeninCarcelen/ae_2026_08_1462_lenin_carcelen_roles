package com.pucetec.roles.services

import com.pucetec.roles.dto.EventRequest
import com.pucetec.roles.entities.Event
import com.pucetec.roles.exceptions.BlankFieldException
import com.pucetec.roles.exceptions.EventNotFoundException
import com.pucetec.roles.exceptions.InvalidCapacityException
import com.pucetec.roles.repositories.EventRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class EventServiceTest {

    @Mock
    lateinit var eventRepository: EventRepository

    private lateinit var eventService: EventService

    @BeforeEach
    fun setUp() {
        eventService = EventService(eventRepository)
    }

    @Test
    fun `should create event when data is valid`() {
        // Arrange
        val request = EventRequest(name = "Festival Quito", venue = "Coliseo General Rumiñahui", totalTickets = 100)
        val savedEvent = Event(id = 1L, name = "Festival Quito", venue = "Coliseo General Rumiñahui", totalTickets = 100, availableTickets = 100)
        whenever(eventRepository.save(any<Event>())).thenReturn(savedEvent)

        // Act
        val response = eventService.createEvent(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals(100, response.totalTickets)
        assertEquals(100, response.availableTickets)
    }

    @Test
    fun `should throw BlankFieldException when name is blank`() {
        // Arrange
        val request = EventRequest(name = "", venue = "Coliseo", totalTickets = 10)

        // Act & Assert
        assertThrows<BlankFieldException> { eventService.createEvent(request) }
    }

    @Test
    fun `should throw BlankFieldException when venue is blank`() {
        // Arrange
        val request = EventRequest(name = "Festival Quito", venue = "", totalTickets = 10)

        // Act & Assert
        assertThrows<BlankFieldException> { eventService.createEvent(request) }
    }

    @Test
    fun `should throw InvalidCapacityException when totalTickets is less than 1`() {
        // Arrange
        val request = EventRequest(name = "Festival Quito", venue = "Coliseo", totalTickets = 0)

        // Act & Assert
        assertThrows<InvalidCapacityException> { eventService.createEvent(request) }
    }

    @Test
    fun `should return all events`() {
        // Arrange
        val events = listOf(
            Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 100),
            Event(id = 2L, name = "Concierto Rock", venue = "Estadio", totalTickets = 50, availableTickets = 50)
        )
        whenever(eventRepository.findAll()).thenReturn(events)

        // Act
        val response = eventService.getAllEvents()

        // Assert
        assertEquals(2, response.size)
    }

    @Test
    fun `should return event by id when it exists`() {
        // Arrange
        val event = Event(id = 1L, name = "Festival Quito", venue = "Coliseo", totalTickets = 100, availableTickets = 100)
        whenever(eventRepository.findById(1L)).thenReturn(Optional.of(event))

        // Act
        val response = eventService.getEventById(1L)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Festival Quito", response.name)
    }

    @Test
    fun `should throw EventNotFoundException when event does not exist`() {
        // Arrange
        whenever(eventRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EventNotFoundException> { eventService.getEventById(99L) }
    }
}
