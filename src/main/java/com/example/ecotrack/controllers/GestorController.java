package com.example.ecotrack.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping
    public String ConsultaUsuario() {
        return "Hello World";
    }

    @GetMapping("/{id}")
    public String ConsultaUsuarioPorId(@PathVariable Long id) {
        return "Usuario por ID " + id;
    }

    @GetMapping("/empresa/{empresaId}")
    public String ConsultarIdEmpresa (@PathVariable Long empresaId){
        return "ID da empresa:" + empresaId;
    }

}
