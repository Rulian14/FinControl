package com.fincontrol.controller.advice;


import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fincontrol.exception.EmailJaRegistradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaRegistradoException.class)
    public ResponseEntity<ProblemDetail> handleEmailJaRegistrado(EmailJaRegistradoException ex) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, 
            ex.getMessage()
        );
        
        problemDetail.setTitle("Conflito de Cadastro");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/email-duplicado"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentials(BadCredentialsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED, ex.getMessage()
        );
        problemDetail.setTitle("Falha na Autenticação");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/credenciais-invalidas"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    // 3. REQUISIÇÃO RUIM (400) - Quando o cliente manda dados inválidos (Validação do Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Coletamos todos os erros de campos e transformamos em uma string melancólica
        String erros = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Dados inválidos fornecidos na requisição."
        );
        problemDetail.setTitle("Erro de Validação");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/validacao-campos"));
        problemDetail.setProperty("erros", erros); // Adiciona a lista de campos errados no JSON
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    // 4. O CATCH-ALL (500) - O último guardião para erros inesperados do sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        // IMPORTANTE: Aqui você pode usar um logger para salvar a stack trace no arquivo de log do servidor,
        // mas para o cliente, nós escondemos o rastro do sangue.
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Ocorreu um erro interno inesperado. Nossos corvos já foram enviados para investigar."
        );
        problemDetail.setTitle("Erro Interno do Servidor");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/erro-interno"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}