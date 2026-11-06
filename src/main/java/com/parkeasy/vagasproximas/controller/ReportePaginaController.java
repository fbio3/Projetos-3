
package com.parkeasy.vagasproximas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportePaginaController {

    @GetMapping("/reportar-problema")
    public String mostrarPagina() {
        return "reportar-problema";
    }
}
