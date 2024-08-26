package com.example.citas.service;

import com.example.citas.model.Cita;
import com.example.citas.repository.CitaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> obtenerCitasPorDoctor(String doctor) {
        return citaRepository.findByDoctor(doctor);
    }
}
