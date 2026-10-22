package com.example.ecotrack.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.ecotrack.DTO.LoginRequest;
import com.example.ecotrack.DTO.LoginResponse;
import com.example.ecotrack.entities.Usuario;
import com.example.ecotrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${spring.secret}")
    private String secret;
    @Value("${spring.emissor}")
    private String emissor;
    @Value("${spring.tempo}")
    private Long tempo;

    @PostMapping
    public ResponseEntity<LoginResponse> Logar(@RequestBody LoginRequest loginRequest){

        if (usuarioRepository.existsUsuarioByCpfAndSenha(loginRequest.getLogin(), loginRequest.getSenha())){

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setMensagem("Login Realizado com Sucesso!");
            //Métodos JWT
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create().withIssuer(emissor).withSubject(loginRequest.getLogin()).withExpiresAt(gerarDataExperiacao()).sign(algorithm);

            loginResponse.setToken(token);

            return ResponseEntity.ok(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private Instant gerarDataExperiacao (){

        LocalDateTime dataAtual = LocalDateTime.now();

        LocalDateTime dataFutura = dataAtual.plusMinutes(tempo);

        return dataFutura.toInstant(ZoneOffset.of("-03:00"));
    }

}
