package com.example.ecotrack.repository;

import com.example.ecotrack.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<List<Material>> getMaterialByNomeMaterial(String nomeMaterial);
}
