package com.example.citas.repository;

import com.example.citas.model.Cita;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CitaRepository {
    private final List<Cita> citas = new ArrayList<>();

    public CitaRepository() {
        // Inicializar con algunas citas de ejemplo
        citas.add(new Cita(1L, "Juan Pérez", "Dr. González", LocalDateTime.of(2024, 8, 26, 10, 0)));
        citas.add(new Cita(2L, "María López", "Dr. Ramírez", LocalDateTime.of(2024, 8, 26, 11, 0)));
        citas.add(new Cita(3L, "Carlos Martínez", "Dr. González", LocalDateTime.of(2024, 8, 27, 9, 30)));
    }

    public List<Cita> findAll() {
        return new ArrayList<>(citas);
    }

    public Optional<Cita> findById(Long id) {
        return citas.stream().filter(cita -> cita.getId().equals(id)).findFirst();
    }

    public List<Cita> findByDoctor(String doctor) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita cita : citas) {
            if (cita.getDoctor().equalsIgnoreCase(doctor)) {
                resultado.add(cita);
            }
        }
        return resultado;
    }
}
