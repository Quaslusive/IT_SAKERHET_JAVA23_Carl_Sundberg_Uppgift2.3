package se.sakerhet.server.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CapsuleDTO {

    private String message;
//Todo Check if needed
    public CapsuleDTO(String message) {
        this.message = message;
    }

}
