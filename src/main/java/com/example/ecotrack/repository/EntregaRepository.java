package com.example.ecotrack.repository;

import com.example.ecotrack.entities.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    Optional<List<Entrega>> getEntregasByIdEntrega(Long idEntrega);
    
}
