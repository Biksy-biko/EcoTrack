package com.example.ecotrack.controllers;

import com.example.ecotrack.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestores")
public class GestorController {

    @GetMapping
    public String ConsultaUsuario() {return "Hello World";}

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
    public ResponseEntity<Usuario> CadastrarUsuario(@RequestBody Usuario usuariorequest){
        return ResponseEntity.ok(usuariorequest);
    }
}
