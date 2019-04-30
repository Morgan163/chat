package lukianov.andrei.chat.listeners;

import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;
import java.util.Objects;

@Component
public class WebSocketEventListener {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private RoomService roomService;


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        User user = (User) Objects.requireNonNull(stompHeaderAccessor.getSessionAttributes()).get("user");
        if (Objects.nonNull(user)) {
            roomService.deleteUserFromRoom(user);
            messagingTemplate.convertAndSend("/topic/room",
                    new Message(user, String.format("%s leaved", user.getName()), new Date()));
        }
    }

}
