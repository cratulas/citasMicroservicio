package com.example.citas.repository;

import com.example.citas.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Métodos adicionales personalizados si son necesarios
}
