package se.sakerhet.server.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CapsuleDTO {

    private String message;

    public CapsuleDTO(String message) {
        this.message = message;
    }
}