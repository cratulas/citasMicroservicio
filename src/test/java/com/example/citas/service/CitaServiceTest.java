package com.example.citas.service;

import com.example.citas.model.Cita;
import com.example.citas.model.Doctor;
import com.example.citas.model.Paciente;
import com.example.citas.repository.CitaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaService citaService;

    @Test
    void testGuardarCita() {
        // Arrange
        Doctor doctor = new Doctor(null, "Dr. Ramirez");
        Paciente paciente = new Paciente(null, "Juan Perez");
        Cita cita = new Cita(null, paciente, doctor, LocalDateTime.of(2024, 9, 27, 10, 0));

        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // Act
        Cita resultado = citaService.guardarCita(cita);

        // Assert
        assertEquals("Juan Perez", resultado.getPaciente().getNombre());
        assertEquals("Dr. Ramirez", resultado.getDoctor().getNombre());
    }
}
