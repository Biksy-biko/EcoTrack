package com.example.ecotrack.repository;

import com.example.ecotrack.entities.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    boolean existsByIdPonto(String IdPonto);

    Optional<List<PontoColeta>> getPontoColetaByIdPonto(Long idPonto);
}
