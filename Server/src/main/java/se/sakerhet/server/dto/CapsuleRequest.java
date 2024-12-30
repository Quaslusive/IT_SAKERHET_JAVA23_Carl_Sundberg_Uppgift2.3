package se.sakerhet.server.dto;

public class CapsuleRequest {
    private String message;

    public CapsuleRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
