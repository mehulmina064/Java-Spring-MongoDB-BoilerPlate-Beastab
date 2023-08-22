package com.beastab.dataservice.identityservice.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
    private static final long serialVersionUID = 1L;
}
