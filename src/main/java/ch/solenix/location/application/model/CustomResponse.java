package ch.solenix.location.application.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "timestamp")
public class CustomResponse {

    /**
     * The custom response timestamp.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime timestamp;

    /**
     * The custom response code.
     */
    private String code;

    /**
     * The custom response message.
     */
    private String message;
}