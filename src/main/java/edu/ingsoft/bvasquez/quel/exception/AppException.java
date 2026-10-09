package edu.ingsoft.bvasquez.quel.exception;

/** Excepción base del dominio. */
public class AppException extends RuntimeException {
    public AppException(String message) { super(message); }
    public AppException(String message, Throwable cause) { super(message, cause); }
}
