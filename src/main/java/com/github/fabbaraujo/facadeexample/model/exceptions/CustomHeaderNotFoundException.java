package com.github.fabbaraujo.facadeexample.model.exceptions;

public class CustomHeaderNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public CustomHeaderNotFoundException(String exception) {
        super(exception);
    }
}
