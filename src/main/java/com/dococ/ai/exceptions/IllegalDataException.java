package com.dococ.ai.exceptions;

public class IllegalDataException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public IllegalDataException(String message) {
        super(message);
    }
}
