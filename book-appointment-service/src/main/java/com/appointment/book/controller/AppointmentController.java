package com.appointment.book.controller;

import com.appointment.book.entity.Appointment;
import com.appointment.book.exception.InvalidTimingsException;
import com.appointment.book.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/dummy")
    public Appointment dummy() {
        Appointment appointment = new Appointment();
        appointment.setAptFor("Dummy");
        appointment.setAptWith("Dummy");
        appointment.setStartTime(LocalDateTime.now());
        appointment.setEndTime(LocalDateTime.now());
        return appointment;
    }

    @PostMapping
    public Appointment save(@Valid @RequestBody Appointment appointment) {
        if(appointment.getStartTime().isAfter(appointment.getEndTime())) {
            throw new InvalidTimingsException("start Time should be before end time");
        }
        return appointmentService.save(appointment);
    }

}
