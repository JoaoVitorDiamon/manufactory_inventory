package com.diamon.manufacturinginventory.Exceptions;

public class InsufficientQuantity extends RuntimeException {
    public InsufficientQuantity(String message) {
        super(message);
    }
}
