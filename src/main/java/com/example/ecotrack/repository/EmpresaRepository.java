package com.example.ecotrack.repository;

import com.example.ecotrack.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> getEmpresaByCnpj(String cnpj);

}
