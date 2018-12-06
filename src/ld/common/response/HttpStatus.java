package ld.common.response;

public enum HttpStatus {

    OK(200),
    MOVE_PERMANENTLY(301),
    FOUND(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500);

    private int status;

    HttpStatus(int status) {
        this.status = status;
    }

    public int code() {
        return status;
    }
}
