package application.utils.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class MessageResponse {
    @NotNull
    private final String message;

    public MessageResponse(@JsonProperty("message") @NotNull String message) {
        this.message = message;
    }

    @NotNull
    public String getMessage() {
        return message;
    }
}
