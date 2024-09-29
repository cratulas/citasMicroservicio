package com.example.citas.controller;

import com.example.citas.model.Cita;
import com.example.citas.model.Doctor;
import com.example.citas.model.Paciente;
import com.example.citas.service.CitaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaController.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaService citaService;

    @Test
    void obtenerCitasPorDoctor() throws Exception {
        Doctor doctor = new Doctor(1L, "Dr. Ramirez");
        Paciente paciente = new Paciente(1L, "Juan Perez");
        Cita cita = new Cita(1L, paciente, doctor, LocalDateTime.of(2024, 9, 27, 10, 0));

        when(citaService.obtenerCitasPorDoctor("Dr. Ramirez")).thenReturn(Arrays.asList(cita));

        mockMvc.perform(get("/api/citas/doctor?nombreDoctor=Dr. Ramirez")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].doctor.nombre").value("Dr. Ramirez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].paciente.nombre").value("Juan Perez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]._links.self.href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]._links.doctor.href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]._links.paciente.href").exists());
    }
}
