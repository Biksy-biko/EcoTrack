package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.AtualizarStatusUsuarioRequest;
import com.example.ecotrack.DTO.AtualizarUsuarioRequest;
import com.example.ecotrack.DTO.AtualizarUsuarioResponse;
import com.example.ecotrack.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/gestores")
public class GestorController {

    @GetMapping
    public String ConsultaUsuario() {
        return "Hello World";
    }

    @GetMapping("/{id}")
    public Usuario ConsultaUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = new Usuario();
        usuario.setNome("Misa");
        usuario.setCpf("931293120481");
        usuario.setDataNascimento("09/12/2007");
        return usuario;
    }

    @GetMapping("/empresa/{empresaId}")
    public Usuario ConsultarIdEmpresa (@PathVariable Long empresaId){
        Usuario usuarioConstrutorCompleto = new Usuario("Samuel","0390213921","27/11/2023");
        return usuarioConstrutorCompleto;
    }
    @PostMapping
    public ResponseEntity<AtualizarUsuarioResponse> CadastrarUsuario(@RequestBody AtualizarUsuarioRequest usuarioRequest){

        Usuario usuariobanco = new Usuario();
        usuariobanco.setNome(usuarioRequest.getNome());
        usuariobanco.setCpf(usuarioRequest.getCpf());
        usuariobanco.setDataNascimento(usuarioRequest.getDataNascimento());

        usuariobanco.setDataCadastro(LocalDateTime.now());
        usuariobanco.setStatus("A");

        return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));

    }

    @PutMapping("/{id}")
    public ResponseEntity<AtualizarUsuarioResponse> AtualizarCadastroUsuario(@PathVariable Long id, @RequestBody AtualizarUsuarioRequest usuarioRequest){

        Usuario usuariobanco = new Usuario();
        if (usuariobanco != null){
            usuariobanco.setNome(usuarioRequest.getNome());
            usuariobanco.setCpf(usuarioRequest.getCpf());
            usuariobanco.setDataNascimento(usuarioRequest.getDataNascimento());
            usuariobanco.setDataAtualizacao(LocalDateTime.now());

            return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));


        }return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AtualizarUsuarioResponse> AtualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusUsuarioRequest usuarioRequest){

        Usuario usuariobanco = new Usuario();

        if (usuariobanco != null){
            usuariobanco.setStatus(usuariobanco.getStatus());

            return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));


        }return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AtualizarUsuarioResponse> DeleteStatus(@PathVariable Long id){

        Usuario usuariobanco = new Usuario();

        if (usuariobanco != null){
            usuariobanco.setStatus("D");

            return ResponseEntity.ok().build();


        }return ResponseEntity.notFound().build();
    }
}
