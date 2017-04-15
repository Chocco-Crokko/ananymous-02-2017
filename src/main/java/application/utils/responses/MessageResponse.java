package application.utils.responses;

import javax.validation.constraints.NotNull;

public class MessageResponse {
    @NotNull
    private final String message;

    public MessageResponse(@NotNull String message) {
        this.message = message;
    }

    @NotNull
    public String getMessage() {
        return message;
    }
}
