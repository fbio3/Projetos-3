package com.parkeasy.vagasproximas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.parkeasy.vagasproximas.controller.TarifaApiController;

import java.util.Map;

/** Restrito ao controller de tarifas para não alterar o tratamento de erros das outras histórias. */
@RestControllerAdvice(assignableTypes = TarifaApiController.class)
public class TarifaExceptionHandler {

    @ExceptionHandler(ConflitoTarifaException.class)
    public ResponseEntity<Map<String, String>> conflito(ConflitoTarifaException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensagem", e.getMessage()));
    }

    @ExceptionHandler(TarifaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> naoEncontrada(TarifaNaoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalido(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("mensagem", "Dados inválidos. Revise os campos do formulário."));
    }
}
