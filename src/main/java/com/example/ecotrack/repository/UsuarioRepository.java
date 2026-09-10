package com.example.ecotrack.repository;

import com.example.ecotrack.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {

    boolean existsUsuarioByCpfAndSenha(String cpf, String senha);

    Optional<Usuario> getUsuarioByCpf(String cpf);

    Optional<List<Usuario>> getUsuariosByStatus(String status);

    String findUsuarioByNome(String nome);

    List<Usuario> nome(String nome);
}
