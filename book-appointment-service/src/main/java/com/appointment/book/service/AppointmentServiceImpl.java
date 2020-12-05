package com.appointment.book.service;

import com.appointment.book.entity.Appointment;
import com.appointment.book.exception.AppointmentConflictException;
import com.appointment.book.repo.AppointmentRepo;
import com.appointment.book.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    Lock writeLock = readWriteLock.writeLock();

    private final AppointmentRepo appointmentRepo;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepo appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public Appointment save(Appointment appointment) {
        try {
            writeLock.lock();
            Appointment ap = appointmentRepo.findByStartTimeBetween(appointment.getStartTime(), appointment.getEndTime());
            if(ap != null && AppConstants.APT_CANCEL.equals(ap.getStatus())) {
                throw new AppointmentConflictException("Appointment having conflict starting time : " + ap.getStartTime());
            }
            return appointmentRepo.save(appointment);
        } finally {
            writeLock.unlock();
        }
    }
}
