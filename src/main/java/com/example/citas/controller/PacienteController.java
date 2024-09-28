package com.example.citas.controller;

import com.example.citas.model.Paciente;
import com.example.citas.service.PacienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public List<Paciente> obtenerTodosLosPacientes() {
        logger.info("Obteniendo todos los pacientes");
        return pacienteService.obtenerTodosLosPacientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPacientePorId(@PathVariable Long id) {
        logger.info("Obteniendo paciente con ID: {}", id);
        Optional<Paciente> paciente = pacienteService.obtenerPacientePorId(id);
        return paciente.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("Paciente con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        });
    }

    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@Valid @RequestBody Paciente paciente) {
        logger.info("Creando nuevo paciente: {}", paciente.getNombre());
        Paciente nuevoPaciente = pacienteService.guardarPaciente(paciente);
        logger.info("Paciente creado con éxito: {}", paciente.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id, @Valid @RequestBody Paciente pacienteActualizado) {
        logger.info("Actualizando paciente con ID: {}", id);
        Optional<Paciente> paciente = pacienteService.obtenerPacientePorId(id);
        if (paciente.isPresent()) {
            pacienteActualizado.setId(id);
            Paciente pacienteGuardado = pacienteService.guardarPaciente(pacienteActualizado);
            logger.info("Paciente actualizado con éxito: {}", pacienteGuardado.getNombre());
            return ResponseEntity.ok(pacienteGuardado);
        } else {
            logger.warn("Paciente con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) {
        logger.info("Eliminando paciente con ID: {}", id);
        Optional<Paciente> paciente = pacienteService.obtenerPacientePorId(id);
        if (paciente.isPresent()) {
            pacienteService.eliminarPaciente(id);
            logger.info("Paciente con ID {} eliminado", id);
            return ResponseEntity.ok("Paciente eliminado con éxito.");
        } else {
            logger.warn("Paciente con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado.");
        }
    }
}
