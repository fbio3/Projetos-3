package com.parkeasy.vagasproximas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TarifaPaginaController {

    @GetMapping("/tarifas")
    public String paginaTarifas() {
        return "tarifas";
    }
}
