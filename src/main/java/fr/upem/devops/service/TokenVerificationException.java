package fr.upem.devops.service;

public class TokenVerificationException extends RuntimeException {
    public TokenVerificationException(Throwable t) {
        super(t);
    }
}
