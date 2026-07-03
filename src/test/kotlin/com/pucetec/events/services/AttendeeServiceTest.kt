package com.pucetec.events.services

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.entities.Attendee
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.repositories.AttendeeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class AttendeeServiceTest {

    @Mock
    lateinit var attendeeRepository: AttendeeRepository

    private lateinit var attendeeService: AttendeeService

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        attendeeService = AttendeeService(attendeeRepository)
    }

    @Test
    fun `should create attendee when data is valid`() {
        // Arrange
        val request = AttendeeRequest(name = "Juan Perez", email = "juan@puce.edu.ec")
        val savedAttendee = Attendee(id = 1L, name = "Juan Perez", email = "juan@puce.edu.ec")
        whenever(attendeeRepository.save(any<Attendee>())).thenReturn(savedAttendee)

        // Act
        val response = attendeeService.createAttendee(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Juan Perez", response.name)
        assertEquals("juan@puce.edu.ec", response.email)
    }

    @Test
    fun `should throw BlankFieldException when name is blank`() {
        // Arrange
        val request = AttendeeRequest(name = "", email = "juan@puce.edu.ec")

        // Act & Assert
        assertThrows<BlankFieldException> { attendeeService.createAttendee(request) }
    }

    @Test
    fun `should throw BlankFieldException when email is blank`() {
        // Arrange
        val request = AttendeeRequest(name = "Juan Perez", email = "")

        // Act & Assert
        assertThrows<BlankFieldException> { attendeeService.createAttendee(request) }
    }
}
