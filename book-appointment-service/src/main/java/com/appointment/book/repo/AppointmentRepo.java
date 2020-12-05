package com.appointment.book.repo;

import com.appointment.book.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    Appointment findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

}
