package com.pucetec.roles.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ExceptionResponse(
    val message: String?,
    val source: String
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BlankFieldException::class)
    fun handleBlankField(ex: BlankFieldException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse(ex.message, "BlankFieldException"))
    }

    @ExceptionHandler(InvalidCapacityException::class)
    fun handleInvalidCapacity(ex: InvalidCapacityException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse(ex.message, "InvalidCapacityException"))
    }

    @ExceptionHandler(AttendeeNotFoundException::class)
    fun handleAttendeeNotFound(ex: AttendeeNotFoundException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ExceptionResponse(ex.message, "AttendeeNotFoundException"))
    }

    @ExceptionHandler(EventNotFoundException::class)
    fun handleEventNotFound(ex: EventNotFoundException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ExceptionResponse(ex.message, "EventNotFoundException"))
    }

    @ExceptionHandler(ReservationNotFoundException::class)
    fun handleReservationNotFound(ex: ReservationNotFoundException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ExceptionResponse(ex.message, "ReservationNotFoundException"))
    }

    @ExceptionHandler(SoldOutException::class)
    fun handleSoldOut(ex: SoldOutException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ExceptionResponse(ex.message, "SoldOutException"))
    }

    @ExceptionHandler(ReservationLimitExceededException::class)
    fun handleReservationLimitExceeded(ex: ReservationLimitExceededException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ExceptionResponse(ex.message, "ReservationLimitExceededException"))
    }

    @ExceptionHandler(ReservationAlreadyCancelledException::class)
    fun handleReservationAlreadyCancelled(ex: ReservationAlreadyCancelledException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ExceptionResponse(ex.message, "ReservationAlreadyCancelledException"))
    }
}
