package com.example.demo.exception.classes;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String message){
        super(message);
    }
}
