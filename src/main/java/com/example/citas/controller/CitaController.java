package com.example.citas.controller;

import com.example.citas.model.Cita;
import com.example.citas.service.CitaService;
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
@RequestMapping("/api/citas")
public class CitaController {

    private static final Logger logger = LoggerFactory.getLogger(CitaController.class);
    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Cita>>> obtenerTodasLasCitas() {
        logger.info("Obteniendo todas las citas");
        List<Cita> citas = citaService.obtenerTodasLasCitas();
        List<EntityModel<Cita>> citasConEnlaces = citas.stream().map(this::agregarEnlaces).collect(Collectors.toList());
        return ResponseEntity.ok(citasConEnlaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cita>> obtenerCitaPorId(@PathVariable Long id) {
        logger.info("Obteniendo cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        return cita.map(c -> ResponseEntity.ok(agregarEnlaces(c)))
                   .orElseGet(() -> {
                       logger.warn("Cita con ID {} no encontrada", id);
                       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                   });
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<EntityModel<Cita>>> obtenerCitasPorDoctor(@RequestParam String nombreDoctor) {
        logger.info("Obteniendo citas para el doctor: {}", nombreDoctor);
        List<Cita> citas = citaService.obtenerCitasPorDoctor(nombreDoctor);
        if (citas.isEmpty()) {
            logger.warn("No se encontraron citas para el doctor: {}", nombreDoctor);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<EntityModel<Cita>> citasConEnlaces = citas.stream().map(this::agregarEnlaces).collect(Collectors.toList());
        return ResponseEntity.ok(citasConEnlaces);
    }

    @GetMapping("/paciente")
    public ResponseEntity<List<EntityModel<Cita>>> obtenerCitasPorPaciente(@RequestParam String nombrePaciente) {
        logger.info("Obteniendo citas para el paciente: {}", nombrePaciente);
        List<Cita> citas = citaService.obtenerCitasPorPaciente(nombrePaciente);
        if (citas.isEmpty()) {
            logger.warn("No se encontraron citas para el paciente: {}", nombrePaciente);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<EntityModel<Cita>> citasConEnlaces = citas.stream().map(this::agregarEnlaces).collect(Collectors.toList());
        return ResponseEntity.ok(citasConEnlaces);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Cita>> crearCita(@RequestBody Cita cita) {
        logger.info("Creando nueva cita para el paciente: {}", cita.getPaciente().getNombre());
        Cita nuevaCita = citaService.guardarCita(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(agregarEnlaces(nuevaCita));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cita>> actualizarCita(@PathVariable Long id, @RequestBody Cita citaActualizada) {
        logger.info("Actualizando cita con ID: {}", id);
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        if (cita.isPresent()) {
            citaActualizada.setId(id);
            Cita citaGuardada = citaService.guardarCita(citaActualizada);
            return ResponseEntity.ok(agregarEnlaces(citaGuardada));
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

    private EntityModel<Cita> agregarEnlaces(Cita cita) {
        EntityModel<Cita> resource = EntityModel.of(cita);
        Link selfLink = linkTo(methodOn(CitaController.class).obtenerCitaPorId(cita.getId())).withSelfRel();
        Link doctorLink = linkTo(methodOn(DoctorController.class).obtenerDoctorPorId(cita.getDoctor().getId())).withRel("doctor");
        Link pacienteLink = linkTo(methodOn(PacienteController.class).obtenerPacientePorId(cita.getPaciente().getId())).withRel("paciente");
        resource.add(selfLink, doctorLink, pacienteLink);
        return resource;
    }
}
