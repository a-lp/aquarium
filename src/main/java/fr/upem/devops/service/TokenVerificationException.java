package fr.upem.devops.authentication;

public class TokenVerificationException extends RuntimeException {
    public TokenVerificationException(Throwable t) {
        super(t);
    }
}
