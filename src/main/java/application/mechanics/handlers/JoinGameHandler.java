package application.mechanics.handlers;

import application.mechanics.MechanicsExecutor;
import application.mechanics.requests.JoinGame;
import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class JoinGameHandler extends MessageHandler<JoinGame.Request> {
    @NotNull
    private final MechanicsExecutor mechanicsExecutor;

    public JoinGameHandler(@NotNull MechanicsExecutor mechanicsExecutor,
                           @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.Request.class);
        this.mechanicsExecutor = mechanicsExecutor;
        messageHandlerContainer.registerHandler(clazz, this);
    }

    @Override
    public void handle(@NotNull JoinGame.Request message, long forUser) throws HandleException {
        mechanicsExecutor.addUser(forUser);
    }
}