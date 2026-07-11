package com.pucetec.roles.repositories

import com.pucetec.events.entities.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long>
