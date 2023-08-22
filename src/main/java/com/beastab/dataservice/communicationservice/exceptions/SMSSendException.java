package com.beastab.dataservice.communicationservice.exceptions;

public class SMSSendException extends RuntimeException{
    public SMSSendException(String message){
        super(message);
    }
    private static final long serialVersionUID = 1L;
}
