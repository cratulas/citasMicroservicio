package com.example.citas.service;

import com.example.citas.model.Doctor;
import com.example.citas.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> obtenerTodosLosDoctores() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> obtenerDoctorPorId(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor guardarDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void eliminarDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
