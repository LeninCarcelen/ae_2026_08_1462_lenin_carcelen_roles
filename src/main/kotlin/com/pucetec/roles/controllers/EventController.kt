package com.pucetec.roles.controllers

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.services.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventResponse>> {
        return ResponseEntity.ok(eventService.getAllEvents())
    }

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long): ResponseEntity<EventResponse> {
        return ResponseEntity.ok(eventService.getEventById(id))
    }

    @PostMapping
    fun createEvent(@RequestBody request: EventRequest): ResponseEntity<EventResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request))
    }
}
