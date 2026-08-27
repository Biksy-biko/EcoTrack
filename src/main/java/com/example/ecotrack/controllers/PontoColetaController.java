package com.example.ecotrack.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pontoscoleta")
public class PontoColetaController {

    @GetMapping("/{id}")
    public String MaterialId(@PathVariable Long id) {
        return "ID do ponto de coleta:" + id;
    }

    @GetMapping("/{local}")
    public String MaterialNome(@PathVariable String local) {
        return "Local:" + local;
    }

}
