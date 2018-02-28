package application.websocket;

import org.jetbrains.annotations.NotNull;

public interface MessageHandlerContainer {

    void handle(@NotNull Message message, long forUser) throws HandleException;

    <T> void registerHandler(@NotNull Class<T> clazz, @NotNull MessageHandler<T> handler);
}


