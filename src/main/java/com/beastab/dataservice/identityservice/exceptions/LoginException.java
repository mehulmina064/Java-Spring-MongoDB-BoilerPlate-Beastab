package com.beastab.dataservice.identityservice.exceptions;

public class LoginException extends RuntimeException{
    public LoginException(String message){
        super(message);
    }
    private static final long serialVersionUID = 1L;
}
