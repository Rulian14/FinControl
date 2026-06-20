package com.fincontrol.controller.advice;


import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fincontrol.exception.CategoriaInvalidaException;
import com.fincontrol.exception.EmailJaRegistradoException;
import com.fincontrol.exception.LimiteTelefoneCadastradosException;
import com.fincontrol.exception.ResourceNotFoundException;
import com.fincontrol.exception.TelefoneJaCadastradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    
    @ExceptionHandler(CategoriaInvalidaException.class)
    public ResponseEntity<ProblemDetail> handleCategoriaInvalida(CategoriaInvalidaException ex) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage()
        );
        
        problemDetail.setTitle("Regra de Negócio Violada");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/categoria-invalida"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(problemDetail);
    }


    @ExceptionHandler(LimiteTelefoneCadastradosException.class)
    public ResponseEntity<ProblemDetail> handleLimiteTelefoneCadastrados(LimiteTelefoneCadastradosException ex) {
        
        // Substituído pelo novo padrão que não carrega o peso do passado
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage()
        );
        
        problemDetail.setTitle("Regra de Negócio Violada");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/limite-telefones-atingido"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(problemDetail);
    }

    @ExceptionHandler(EmailJaRegistradoException.class)
    public ResponseEntity<ProblemDetail> handleEmailJaRegistrado(EmailJaRegistradoException ex) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, ex.getMessage()
        );
        
        problemDetail.setTitle("Conflito de Cadastro");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/email-duplicado"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }
    
    @ExceptionHandler(TelefoneJaCadastradoException.class)
    public ResponseEntity<ProblemDetail> handleTelefoneJaRegistrado(TelefoneJaCadastradoException ex){

         ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, ex.getMessage()
        );
        
        problemDetail.setTitle("Conflito de Cadastro");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/Telefone-duplicado"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex){
         ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage()
        );

        problemDetail.setTitle("Item não Encotrado");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/recurso-nao-encontrado"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentials(BadCredentialsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos."
        );
        problemDetail.setTitle("Falha na Autenticação");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/credenciais-invalidas"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

   
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex) {
        var erros = ex.getBindingResult()
                      .getFieldErrors()
                      .stream()
                      .collect(Collectors.toMap(error -> error.getField(), error -> error.getDefaultMessage(), (v1, v2) -> v1 + ", " + v2));
                

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Dados inválidos fornecidos na requisição."
        );
        problemDetail.setTitle("Erro de Validação");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/validacao-campos"));
        problemDetail.setProperty("erros", erros);
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
    
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {

        UUID uuid = UUID.randomUUID();
        log.error("Erro inesperado[ID: {}]", uuid, ex);
       
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Ocorreu um erro interno inesperado. Nossos corvos já foram enviados para investigar.");

        problemDetail.setTitle("Erro Interno do Servidor");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/erro-interno"));
        problemDetail.setProperty("erro_id", uuid);
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}