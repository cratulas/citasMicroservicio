package com.example.citas.controller;

import com.example.citas.model.Doctor;
import com.example.citas.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/doctores")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<EntityModel<Doctor>> obtenerTodosLosDoctores() {
        logger.info("Obteniendo todos los doctores");
        List<Doctor> doctores = doctorService.obtenerTodosLosDoctores();
        return doctores.stream().map(this::agregarEnlaces).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Doctor>> obtenerDoctorPorId(@PathVariable Long id) {
        logger.info("Obteniendo doctor con ID: {}", id);
        Optional<Doctor> doctor = doctorService.obtenerDoctorPorId(id);
        return doctor.map(d -> ResponseEntity.ok(agregarEnlaces(d)))
                .orElseGet(() -> {
                    logger.warn("Doctor con ID {} no encontrado", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    @PostMapping
    public ResponseEntity<EntityModel<Doctor>> crearDoctor(@RequestBody Doctor doctor) {
        logger.info("Creando nuevo doctor: {}", doctor.getNombre());
        Doctor nuevoDoctor = doctorService.guardarDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(agregarEnlaces(nuevoDoctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Doctor>> actualizarDoctor(@PathVariable Long id, @RequestBody Doctor doctorActualizado) {
        logger.info("Actualizando doctor con ID: {}", id);
        Optional<Doctor> doctor = doctorService.obtenerDoctorPorId(id);
        if (doctor.isPresent()) {
            doctorActualizado.setId(id);
            Doctor doctorGuardado = doctorService.guardarDoctor(doctorActualizado);
            return ResponseEntity.ok(agregarEnlaces(doctorGuardado));
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
            return ResponseEntity.ok("Doctor eliminado con éxito.");
        } else {
            logger.warn("Doctor con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor no encontrado.");
        }
    }

    private EntityModel<Doctor> agregarEnlaces(Doctor doctor) {
        EntityModel<Doctor> resource = EntityModel.of(doctor);
        Link selfLink = linkTo(methodOn(DoctorController.class).obtenerDoctorPorId(doctor.getId())).withSelfRel();
        resource.add(selfLink);
        return resource;
    }
}
