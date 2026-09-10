package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.LoginRequest;
import com.example.ecotrack.DTO.LoginResponse;
import com.example.ecotrack.entities.Usuario;
import com.example.ecotrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<LoginResponse> Logar(@RequestBody LoginRequest loginRequest){

        //Usuario usuariobanco = usuarioRepository.findAll();

        if (usuarioRepository.existsUsuarioByCpfAndSenha(loginRequest.getLogin(), loginRequest.getSenha())){

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setMensagem("Login Realizado com Sucesso!");

            return ResponseEntity.ok(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
