package com.example.citas.repository;

import com.example.citas.model.Cita;
import com.example.citas.model.Doctor;
import com.example.citas.model.Paciente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
class CitaRepositoryTest {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    @Rollback(false)
    void testGuardarYEncontrarCita() {
        // Primero guardar el Doctor y el Paciente
        Doctor doctor = new Doctor(null, "Dr. Ramirez");
        Paciente paciente = new Paciente(null, "Juan Perez");

        doctor = doctorRepository.save(doctor); // Persistir el doctor
        paciente = pacienteRepository.save(paciente); // Persistir el paciente

        // Crear la cita
        Cita cita = new Cita(null, paciente, doctor, LocalDateTime.of(2024, 9, 27, 10, 0));
        cita = citaRepository.save(cita);

        // Verificar que la cita se guardó correctamente
        assertNotNull(cita.getId());

        // Obtener todas las citas
        List<Cita> citas = citaRepository.findAll();
        assertTrue(citas.size() > 0);
    }
}
