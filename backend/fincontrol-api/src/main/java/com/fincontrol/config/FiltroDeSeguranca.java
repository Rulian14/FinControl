package com.fincontrol.config;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fincontrol.exception.TokenInvalidoException;
import com.fincontrol.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltroDeSeguranca extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public FiltroDeSeguranca(JwtService jwtService){
        this.jwtService = jwtService;
        this.objectMapper = new ObjectMapper();
    } 

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarToken(request);

            if(token != null){
                try {
                    String idUsuario = jwtService.extrairID(token);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(idUsuario, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }catch (TokenInvalidoException ex) {
                    SecurityContextHolder.clearContext();
                    responderErroToken(request, response, ex.getMessage());
                    return;
                }
               
            }
            filterChain.doFilter(request, response);
        }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    private void responderErroToken(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String mensagem) throws IOException {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                mensagem
        );

        problemDetail.setTitle("Falha na Autenticação");
        problemDetail.setType(URI.create("https://api.fincontrol.com/errors/token-invalido"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), problemDetail);
    }
}   
