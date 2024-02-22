package com.graduation.topjava.util.exception;

public class ExistException extends RuntimeException {
    public ExistException(String message) {
        super("Meal with the name and date: " +  message + " for this restaurant already exists");
    }
}