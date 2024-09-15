package com.example.citas.controller;

import com.example.citas.model.Cita;
import com.example.citas.service.CitaService;
import jakarta.validation.Valid;
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

    // GET: Obtener todas las citas
    @GetMapping
    public List<Cita> obtenerTodasLasCitas() {
        logger.info("Obteniendo todas las citas");
        return citaService.obtenerTodasLasCitas();
    }

    // GET: Obtener cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable Long id) {
        logger.info("Obteniendo cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        return cita.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("Cita con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        });
    }

    // GET: Obtener citas por doctor
    @GetMapping("/doctor")
    public List<Cita> obtenerCitasPorDoctor(@RequestParam String doctor) {
        logger.info("Obteniendo citas del doctor: {}", doctor);
        return citaService.obtenerCitasPorDoctor(doctor);
    }

    // POST: Crear una nueva cita
    @PostMapping
    public ResponseEntity<Cita> crearCita(@Valid @RequestBody Cita cita) {
        logger.info("Creando nueva cita para el paciente: {}", cita.getPaciente());
        Cita nuevaCita = citaService.guardarCita(cita);
        logger.info("Cita creada con éxito para el paciente: {}", cita.getPaciente());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }

    // PUT: Actualizar una cita existente
    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @Valid @RequestBody Cita citaActualizada) {
        logger.info("Actualizando cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        if (cita.isPresent()) {
            citaActualizada.setId(id);  // Asegurarse de que se actualiza la cita correcta
            Cita citaGuardada = citaService.guardarCita(citaActualizada);
            logger.info("Cita actualizada con éxito para el paciente: {}", citaGuardada.getPaciente());
            return ResponseEntity.ok(citaGuardada);
        } else {
            logger.warn("Cita con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // DELETE: Eliminar una cita
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCita(@PathVariable Long id) {
        logger.info("Eliminando cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        if (cita.isPresent()) {
            citaService.eliminarCita(id);
            logger.info("Cita con ID {} eliminada", id);
            return ResponseEntity.ok("Cita eliminada con éxito.");
        } else {
            logger.warn("Cita con ID {} no encontrada", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cita no encontrada.");
        }
    }
}
