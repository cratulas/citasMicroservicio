package com.example.citas.controller;

import com.example.citas.model.Cita;
import com.example.citas.service.CitaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private static final Logger logger = LoggerFactory.getLogger(CitaController.class);
    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public List<Cita> obtenerTodasLasCitas() {
        logger.info("Obteniendo todas las citas");
        return citaService.obtenerTodasLasCitas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable Long id) {
        logger.info("Obteniendo cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        return cita.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("Cita con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        });
    }

    // Endpoint para obtener citas por el nombre del doctor
    @GetMapping("/doctor")
    public ResponseEntity<List<Cita>> obtenerCitasPorDoctor(@RequestParam String nombreDoctor) {
        logger.info("Obteniendo citas para el doctor: {}", nombreDoctor);
        List<Cita> citas = citaService.obtenerCitasPorDoctor(nombreDoctor);
        if (citas.isEmpty()) {
            logger.warn("No se encontraron citas para el doctor: {}", nombreDoctor);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(citas);
    }

    // Endpoint para obtener citas por el nombre del paciente
    @GetMapping("/paciente")
    public ResponseEntity<List<Cita>> obtenerCitasPorPaciente(@RequestParam String nombrePaciente) {
        logger.info("Obteniendo citas para el paciente: {}", nombrePaciente);
        List<Cita> citas = citaService.obtenerCitasPorPaciente(nombrePaciente);
        if (citas.isEmpty()) {
            logger.warn("No se encontraron citas para el paciente: {}", nombrePaciente);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(citas);
    }


    @PostMapping
    public ResponseEntity<Cita> crearCita(@RequestBody Cita cita) {
        logger.info("Creando nueva cita para el paciente: {}", cita.getPaciente().getNombre());
        Cita nuevaCita = citaService.guardarCita(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita citaActualizada) {
        logger.info("Actualizando cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        if (cita.isPresent()) {
            citaActualizada.setId(id);
            Cita citaGuardada = citaService.guardarCita(citaActualizada);
            return ResponseEntity.ok(citaGuardada);
        } else {
            logger.warn("Cita con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCita(@PathVariable Long id) {
        logger.info("Eliminando cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        if (cita.isPresent()) {
            citaService.eliminarCita(id);
            return ResponseEntity.ok("Cita eliminada con éxito.");
        } else {
            logger.warn("Cita con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cita no encontrada.");
        }
    }
}
