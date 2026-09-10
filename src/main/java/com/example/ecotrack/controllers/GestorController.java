package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.AtualizarStatusUsuarioRequest;
import com.example.ecotrack.DTO.AtualizarUsuarioRequest;
import com.example.ecotrack.DTO.AtualizarUsuarioResponse;
import com.example.ecotrack.entities.Usuario;
import com.example.ecotrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/gestores")
public class GestorController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> ConsultaUsuario() {

        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> ConsultaUsuarioPorId(@PathVariable Long id) {
        var usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
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
        usuariobanco.setSenha(usuarioRequest.getSenha());

        usuariobanco.setDataCadastro(LocalDateTime.now());
        usuariobanco.setStatus("A");

        //Salvando no banco
        usuarioRepository.save(usuariobanco);

        return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));

    }

    @PutMapping("/{id}")
    public ResponseEntity<AtualizarUsuarioResponse> AtualizarCadastroUsuario(@PathVariable Long id, @RequestBody AtualizarUsuarioRequest usuarioRequest){

        Usuario usuariobanco = usuarioRepository.findById(id).orElse(null);
        if (usuariobanco != null){
            usuariobanco.setNome(usuarioRequest.getNome());
            usuariobanco.setCpf(usuarioRequest.getCpf());
            usuariobanco.setDataNascimento(usuarioRequest.getDataNascimento());
            usuariobanco.setDataAtualizacao(LocalDateTime.now());
            usuariobanco.setSenha(usuarioRequest.getSenha());
            usuarioRepository.save(usuariobanco);
            return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));


        }return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AtualizarUsuarioResponse> AtualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusUsuarioRequest usuarioRequest){

        Usuario usuariobanco = usuarioRepository.findById(id).orElse(null);

        if (usuariobanco != null){
            usuariobanco.setStatus(usuarioRequest.getStatus());
            usuarioRepository.save(usuariobanco);
            return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));


        }return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AtualizarUsuarioResponse> DeleteStatus(@PathVariable Long id){

        Usuario usuariobanco = usuarioRepository.findById(id).orElse(null);

        if (usuariobanco != null){
            usuariobanco.setStatus("D");
            usuarioRepository.save(usuariobanco);
            return ResponseEntity.ok().build();


        }return ResponseEntity.notFound().build();
    }
}
