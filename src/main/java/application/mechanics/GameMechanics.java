package application.mechanics;

import application.mechanics.avatar.GameUser;
import application.mechanics.base.UserSnap;
import application.mechanics.internal.ClientSnapService;
import application.mechanics.internal.GameSessionService;
import application.mechanics.internal.ServerSnapService;
import application.models.User;
import application.services.AccountService;
import application.websocket.Message;
import application.websocket.RemotePointService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameMechanics {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanics.class.getSimpleName());

    @NotNull
    private final AccountService accountService;
    @NotNull
    private final ClientSnapService clientSnapshotsService;
    @NotNull
    private final ServerSnapService serverSnapshotService;
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final GameSessionService gameSessionService;

    @NotNull
    private final ConcurrentLinkedQueue<Long> waiters = new ConcurrentLinkedQueue<>();
    @NotNull
    private final ConcurrentLinkedQueue<Long> deleted = new ConcurrentLinkedQueue<>();
    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    @NotNull
    private final ObjectMapper objectMapper;

    public GameMechanics(@NotNull AccountService accountService,
                         @NotNull ServerSnapService serverSnapshotService,
                         @NotNull RemotePointService remotePointService,
                         @NotNull ClientSnapService clientSnapService,
                         @NotNull ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.serverSnapshotService = serverSnapshotService;
        this.remotePointService = remotePointService;
        this.gameSessionService = new GameSessionService(remotePointService);
        this.clientSnapshotsService = clientSnapService;
        this.objectMapper = objectMapper;
    }

    public void addClientSnapshot(long userId, UserSnap userSnap) {
        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, userSnap));
    }

    public int getSessionsNum() {
        return gameSessionService.getSessions().size();
    }

    public void addUser(long user) {
        if (gameSessionService.isPlaying(user)) {
            return;
        }
        waiters.add(user);
    }

    public boolean hasFreeSlots() {
        return gameSessionService.hasFreeSlots();
    }

    public boolean removeUser(long user) {
        if (!gameSessionService.isPlaying(user)) {
            return false;
        }
        deleted.add(user);
        return true;
    }

    public boolean isPlaying(long user) {
        return gameSessionService.isPlaying(user);
    }

    private boolean insureCandidate(long candidate) {
        return remotePointService.isConnected(candidate) &&
                accountService.getUser(candidate) != null;
    }

    public void gmStep() {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            try {
                nextTask.run();
            } catch (RuntimeException ex) {
                LOGGER.error("Cant handle game task", ex);
            }
        }

        for (GameSession session : gameSessionService.getSessions()) {
            clientSnapshotsService.processSnapshotsFor(session);
        }

        final long currentTime = System.nanoTime();
        final Iterator<GameSession> iterator = gameSessionService.getSessions().iterator();
        final Collection<GameSession> sessionsToTerminate = new ArrayList<>();
        while (iterator.hasNext()) {
            final GameSession session = iterator.next();
            final long secondsLeft = session.getRemainingSeconds(currentTime);
            if (secondsLeft == 0) {
                gameSessionService.notifyGameIsOver(session, CloseStatus.NORMAL);
                continue;
            }
            try {
                serverSnapshotService.sendSnapshotsFor(session, secondsLeft);
            } catch (RuntimeException ex) {
                sessionsToTerminate.add(session);
                LOGGER.error("Session was terminated!");
            }
            sessionsToTerminate.forEach((s) -> gameSessionService.notifyGameIsOver(s, CloseStatus.SERVER_ERROR));
        }

        removeLeftUsers();

        while (!waiters.isEmpty()) {
            final Long candidate = waiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            final User newPlayer = accountService.getUser(candidate);
            gameSessionService.addNewPlayer(newPlayer);
        }

        clientSnapshotsService.clear();
    }

    private void removeLeftUsers() {
        final Map<GameSession, List<Long>> sessionLeftPlayers = new HashMap<>();
        while (!deleted.isEmpty()) {
            final long removedPlayer = deleted.poll();

            final GameSession session = gameSessionService.getSessionForUser(removedPlayer);
            gameSessionService.removePlayer(session, removedPlayer);

            sessionLeftPlayers.putIfAbsent(session, new ArrayList<>());
            final List<Long> leftUsers = sessionLeftPlayers.get(session);
            leftUsers.add(removedPlayer);
        }

        for (GameSession session : sessionLeftPlayers.keySet()) {
            final List<Long> playersLeft = sessionLeftPlayers.get(session);
            if (playersLeft.isEmpty()) {
                continue;
            }

            final String jsonArray;
            try {
                jsonArray = objectMapper.writeValueAsString(playersLeft);
            }
            catch (JsonProcessingException e) {
                LOGGER.error("Error serializing!");
                continue;
            }
            final Message message = new Message(Message.REMOVE_PLAYER, jsonArray);
            for (GameUser user : session.getPlayers()) {
                try {
                    remotePointService.sendMessageToUser(user.getId(), message);
                } catch (IOException e) {
                    LOGGER.error("Error sending info about removing user(-s) to user {}", user.getId());
                }
            }
        }
    }

    public void reset() {
        final Set<GameSession> sessions = gameSessionService.getSessions();
        for (GameSession session: sessions) {
            gameSessionService.notifyGameIsOver(session, CloseStatus.SERVER_ERROR);
        }
    }
}


