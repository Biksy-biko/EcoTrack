package com.example.ecotrack.repository;

import com.example.ecotrack.entities.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.logging.LogManager;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
}
