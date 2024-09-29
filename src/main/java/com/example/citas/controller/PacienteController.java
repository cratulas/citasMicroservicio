package com.example.citas.controller;

import com.example.citas.model.Paciente;
import com.example.citas.service.PacienteService;
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
@RequestMapping("/api/pacientes")
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public List<EntityModel<Paciente>> obtenerTodosLosPacientes() {
        logger.info("Obteniendo todos los pacientes");
        List<Paciente> pacientes = pacienteService.obtenerTodosLosPacientes();
        return pacientes.stream().map(this::agregarEnlaces).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Paciente>> obtenerPacientePorId(@PathVariable Long id) {
        logger.info("Obteniendo paciente con ID: {}", id);
        Optional<Paciente> paciente = pacienteService.obtenerPacientePorId(id);
        return paciente.map(p -> ResponseEntity.ok(agregarEnlaces(p)))
                .orElseGet(() -> {
                    logger.warn("Paciente con ID {} no encontrado", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    @PostMapping
    public ResponseEntity<EntityModel<Paciente>> crearPaciente(@RequestBody Paciente paciente) {
        logger.info("Creando nuevo paciente: {}", paciente.getNombre());
        Paciente nuevoPaciente = pacienteService.guardarPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(agregarEnlaces(nuevoPaciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Paciente>> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente pacienteActualizado) {
        logger.info("Actualizando paciente con ID: {}", id);
        Optional<Paciente> paciente = pacienteService.obtenerPacientePorId(id);
        if (paciente.isPresent()) {
            pacienteActualizado.setId(id);
            Paciente pacienteGuardado = pacienteService.guardarPaciente(pacienteActualizado);
            return ResponseEntity.ok(agregarEnlaces(pacienteGuardado));
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
            return ResponseEntity.ok("Paciente eliminado con éxito.");
        } else {
            logger.warn("Paciente con ID {} no encontrado", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado.");
        }
    }

    private EntityModel<Paciente> agregarEnlaces(Paciente paciente) {
        EntityModel<Paciente> resource = EntityModel.of(paciente);
        Link selfLink = linkTo(methodOn(PacienteController.class).obtenerPacientePorId(paciente.getId())).withSelfRel();
        resource.add(selfLink);
        return resource;
    }
}
