// SendMessageResponseDTO.java
package Birger.SMS.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SendMessageResponseDTO(
        String numeroExpediteur,
        String numeroDestinataire,
        String message,
        String status,
        Integer errorCode,
        String errorMessage,
        String sid,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime dateCreated,

        String type
) { }