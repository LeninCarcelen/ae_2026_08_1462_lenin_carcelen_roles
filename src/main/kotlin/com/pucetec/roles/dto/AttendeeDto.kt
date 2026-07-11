package com.pucetec.roles.dto

data class AttendeeRequest(
    val name: String,
    val email: String
)

data class AttendeeResponse(
    val id: Long,
    val name: String,
    val email: String
)
