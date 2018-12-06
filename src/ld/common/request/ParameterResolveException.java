package ld.common.request;

public class ParameterResolveException extends Exception {
    public ParameterResolveException() {
    }

    public ParameterResolveException(String message) {
        super(message);
    }

    public ParameterResolveException(String message, Throwable cause) {
        super(message, cause);
    }
}
