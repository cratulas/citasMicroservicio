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

    public List<Cita> obtenerCitasPorDoctor(String nombreDoctor) {
        return citaRepository.findByDoctor_Nombre(nombreDoctor);
    }

    public List<Cita> obtenerCitasPorPaciente(String nombrePaciente) {
        return citaRepository.findByPaciente_Nombre(nombrePaciente);
    }

    public Cita guardarCita(Cita cita) {
        return citaRepository.save(cita);
    }

    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }
}
