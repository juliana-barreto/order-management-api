package com.senai.ecommerce.shared;

import com.senai.ecommerce.shared.exceptions.DadosInvalidosException;
import com.senai.ecommerce.shared.exceptions.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Tratamento para ERRO 404
  @ExceptionHandler(RecursoNaoEncontradoException.class)
  public ResponseEntity<RespostaErro> recursoNaoEncontrado(
      RecursoNaoEncontradoException ex, HttpServletRequest request) {

    RespostaErro erro = new RespostaErro(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        "Recurso não encontrado",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
  }

  // Tratamento para ERRO 400
  @ExceptionHandler(DadosInvalidosException.class)
  public ResponseEntity<RespostaErro> dadosInvalidos(
      DadosInvalidosException ex, HttpServletRequest request) {

    RespostaErro erro = new RespostaErro(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        "Dados inválidos",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
  }

  // Tratamento para ERRO 500
  @ExceptionHandler(Exception.class)
  public ResponseEntity<RespostaErro> exception(
      Exception ex, HttpServletRequest request) {

    RespostaErro erro = new RespostaErro(
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Erro interno do servidor",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
  }
}
