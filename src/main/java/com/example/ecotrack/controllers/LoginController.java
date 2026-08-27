package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.LoginRequest;
import com.example.ecotrack.DTO.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<LoginResponse> Logar(@RequestBody LoginRequest loginRequest){

        if (loginRequest.getLogin().equals("string") && loginRequest.getSenha().equals("String")){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setMensagem("Bem vindo ao EcoTrack!");

            return ResponseEntity.ok(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
