package com.pucetec.events.controllers

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.services.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val reservationService: ReservationService
) {

    @GetMapping
    fun getAllReservations(): ResponseEntity<List<ReservationResponse>> {
        return ResponseEntity.ok(reservationService.getAllReservations())
    }

    @PostMapping
    fun createReservation(@RequestBody request: ReservationRequest): ResponseEntity<ReservationResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(request))
    }

    @PutMapping("/{id}/cancel")
    fun cancelReservation(@PathVariable id: Long): ResponseEntity<ReservationResponse> {
        return ResponseEntity.ok(reservationService.cancelReservation(id))
    }
}
