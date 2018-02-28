package application.mechanics.handlers;

import application.mechanics.MechanicsExecutor;
import application.mechanics.requests.Disconnect;
import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class DisconnectUserHandler extends MessageHandler<Disconnect.Request> {
    @NotNull
    private final MechanicsExecutor mechanicExecutor;

    public DisconnectUserHandler(@NotNull MechanicsExecutor mechanicsExecutor,
                                 @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(Disconnect.Request.class);
        this.mechanicExecutor = mechanicsExecutor;
        messageHandlerContainer.registerHandler(clazz, this);
    }

    @Override
    public void handle(@NotNull Disconnect.Request message, long forUser) throws HandleException {
        mechanicExecutor.removeUser(forUser);
    }
}


