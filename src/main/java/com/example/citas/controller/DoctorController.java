package com.example.citas.controller;

import com.example.citas.model.Doctor;
import com.example.citas.service.DoctorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctores")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<Doctor> obtenerTodosLosDoctores() {
        logger.info("Obteniendo todos los doctores");
        return doctorService.obtenerTodosLosDoctores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> obtenerDoctorPorId(@PathVariable Long id) {
        logger.info("Obteniendo doctor con ID: {}", id);
        Optional<Doctor> doctor = doctorService.obtenerDoctorPorId(id);
        return doctor.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("Doctor con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        });
    }

    @PostMapping
    public ResponseEntity<Doctor> crearDoctor(@Valid @RequestBody Doctor doctor) {
        logger.info("Creando nuevo doctor: {}", doctor.getNombre());
        Doctor nuevoDoctor = doctorService.guardarDoctor(doctor);
        logger.info("Doctor creado con éxito: {}", doctor.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDoctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> actualizarDoctor(@PathVariable Long id, @Valid @RequestBody Doctor doctorActualizado) {
        logger.info("Actualizando doctor con ID: {}", id);
        Optional<Doctor> doctor = doctorService.obtenerDoctorPorId(id);
        if (doctor.isPresent()) {
            doctorActualizado.setId(id);
            Doctor doctorGuardado = doctorService.guardarDoctor(doctorActualizado);
            logger.info("Doctor actualizado con éxito: {}", doctorGuardado.getNombre());
            return ResponseEntity.ok(doctorGuardado);
        } else {
            logger.warn("Doctor con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDoctor(@PathVariable Long id) {
        logger.info("Eliminando doctor con ID: {}", id);
        Optional<Doctor> doctor = doctorService.obtenerDoctorPorId(id);
        if (doctor.isPresent()) {
            doctorService.eliminarDoctor(id);
            logger.info("Doctor con ID {} eliminado", id);
            return ResponseEntity.ok("Doctor eliminado con éxito.");
        } else {
            logger.warn("Doctor con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor no encontrado.");
        }
    }
}
