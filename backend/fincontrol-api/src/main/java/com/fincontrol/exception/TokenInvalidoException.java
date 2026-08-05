package com.fincontrol.exception;

public class TokenInvalidoException extends RuntimeException{
    public TokenInvalidoException(String msg){
        super(msg);
    }
     public TokenInvalidoException(String msg, Throwable causa) {
        super(msg, causa);
    }
}
