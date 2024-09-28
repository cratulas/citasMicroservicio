package com.example.citas.repository;

import com.example.citas.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    // Buscar citas por el nombre del doctor
    List<Cita> findByDoctor_Nombre(String nombreDoctor);

    // Buscar citas por el nombre del paciente
    List<Cita> findByPaciente_Nombre(String nombrePaciente);
}
