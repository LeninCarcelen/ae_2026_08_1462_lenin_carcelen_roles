package com.pucetec.roles.controllers

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.dto.AttendeeResponse
import com.pucetec.events.services.AttendeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/attendees")
class AttendeeController(
    private val attendeeService: AttendeeService
) {

    @PostMapping
    fun createAttendee(@RequestBody request: AttendeeRequest): ResponseEntity<AttendeeResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendeeService.createAttendee(request))
    }
}
