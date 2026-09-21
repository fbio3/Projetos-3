package com.parkeasy.vagasproximas.controller;

import com.parkeasy.vagasproximas.model.Vaga;
import com.parkeasy.vagasproximas.service.VagaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.util.List;


@Controller
public class VagasController {

    private final VagaService service;

    public VagasController(VagaService service) {
        this.service = service;

    }
    @GetMapping("/")
    public String paginaReact() {
    return "index";
    }

    @GetMapping("/vagas")
    public String listarVagasProximas(Model model) {
        model.addAttribute("vagas", service.listarVagas());
        return "vagas";
    }

    @GetMapping("/vagas/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("vaga", new Vaga());
        return "formulario-vaga";
    }

    @GetMapping("/vagas/salvar")
    public String salvar(@Valid Vaga vaga, BindingResult result) {
        if (result.hasErrors()) {
            return "formulario-vaga";
        }
        service.cadastrarVaga(vaga);
        return "redirect:/vagas";
    }

    @PostMapping("vagas/{id}/remover")
    public String remover(@PathVariable Long id) {
        service.removerVaga(id);
        return "redirect:/vagas";
    }

    @GetMapping("/vagas/mapa")
    public String mapa(Model model) {
        model.addAttribute("raioPadrao", 500.0);
        model.addAttribute("msgPermissao", "Para localizar vagas próximas, é necessário permitir o acesso à sua localização.");
        return "mapa";
    }
    
     // Busca vagas dentro do raio (em metros) a partir da localização informada. 
     // lat e lng são obrigatórios: sem eles a resposta é 400 (nada é assumido).

    @GetMapping("/vagas/proximas")
    @ResponseBody
    public List<Vaga> proximas(
        @RequestParam @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double lat,
        @RequestParam @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double lon,
        @RequestParam(defaultValue = "500") Double raio) {
        return service.buscarProximas(lat, lon, raio);
    }
}

    

    

    