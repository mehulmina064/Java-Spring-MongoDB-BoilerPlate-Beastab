package com.beastab.dataservice.identityservice.exceptions;

public class UserRegistrationException extends RuntimeException{
    public UserRegistrationException(String message){
        super(message);
    }
    private static final long serialVersionUID = 1L;
}
