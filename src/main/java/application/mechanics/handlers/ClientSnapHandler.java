package application.mechanics.handlers;

import application.mechanics.MechanicsExecutor;
import application.mechanics.base.UserSnap;
import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ClientSnapHandler extends MessageHandler<UserSnap> {
    @NotNull
    MechanicsExecutor mechanicsExecutor;

    public ClientSnapHandler(@NotNull MechanicsExecutor mechanicsExecutor,
                             @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(UserSnap.class);
        this.mechanicsExecutor = mechanicsExecutor;
        messageHandlerContainer.registerHandler(clazz, this);
    }

    @Override
    public void handle(@NotNull UserSnap snap, long forUser) throws HandleException {
        snap.setId(forUser);
        mechanicsExecutor.addClientSnapshot(forUser, snap);
    }
}

