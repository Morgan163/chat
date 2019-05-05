package lukianov.andrei.chat.listeners;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.services.impl.RoomServiceImpl;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;
    private final RoomServiceImpl roomServiceImpl;


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        User user = (User) Objects.requireNonNull(stompHeaderAccessor.getSessionAttributes()).get("owner");
        if (Objects.nonNull(user)) {
            roomServiceImpl.deleteUserFromRoom(user);
            messagingTemplate.convertAndSend("/topic/room",
                    new Message(user, String.format("%s leaved", user.getLogin()), new Date()));
        }
    }

}
