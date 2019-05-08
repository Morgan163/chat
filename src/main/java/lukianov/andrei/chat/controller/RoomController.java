package lukianov.andrei.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lukianov.andrei.chat.model.*;
import lukianov.andrei.chat.services.ClientMessageService;
import lukianov.andrei.chat.services.impl.RoomServiceImpl;
import lukianov.andrei.chat.services.impl.UserServiceImpl;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Controller
public class RoomController {

    private final RoomServiceImpl roomServiceImpl;

    private final SimpMessageSendingOperations messagingTemplate;

    private final ClientMessageService clientMessageService;

    private final UserServiceImpl userService;

    /*@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/room")
    public Message sendMessage(@Payload Message message) {
        message.setDate(new Date());
        roomServiceImpl.addMessageToRoom(message);
        return message;
    }*/

    /*@MessageMapping("/chat.addUserToRoom")
    @SendToUser("/queue/reply")
    public List<Message> addUserToRoom(@Payload User user) {
        log.info("user connected " + user.getLogin());
        return roomServiceImpl.getAllMessages();
    }

    @MessageMapping("/chat.notifyOtherUsers")
    @SendTo("/topic/room")
    public Message notifyUsersAboutNewConnect(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        if (roomServiceImpl.addUserToRoom(user)) {
            log.info(user.getLogin() + " was added to room");
            Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("owner", user);
            return new Message(user, String.format("%s joined", user.getLogin()), new Date());
        }
        return new Message(user, "");
    }*/

    @MessageMapping("/chat.connect")
    @SendToUser("/queue/reply/rooms")
    public List<Room> connectMessage(@Payload ClientMessage clientMessage) {
        return userService.getUserByLogin(clientMessage.getUsername()).getRooms();
    }

    @MessageMapping("/chat.general")
    @SendToUser("/queue/reply")
    public Message generalMessage(@Payload ClientMessage clientMessage, SimpMessageHeaderAccessor headerAccessor) {
        return clientMessageService.createMessage(clientMessage);
    }

    @MessageMapping("/chat/{roomName}/sendMessage")
    public void sendMessage(@DestinationVariable String roomName, @Payload ClientMessage clientMessage) {
        Message message = clientMessageService.createMessage(clientMessage);
        if (message.getMessageType().equals(MessageType.CONNECT) && Objects.nonNull(message.getMessageAbout())) {
            messagingTemplate.convertAndSendToUser(message.getMessageAbout().getLogin(), "/queue/reply",
                    message);
        }
        messagingTemplate.convertAndSend("/topic/" + roomName, message);
    }

    @MessageMapping("/chat/{roomName}/addUser")
    public void addUser(@DestinationVariable String roomName, @Payload ClientMessage clientMessage, SimpMessageHeaderAccessor headerAccessor) {
        Message message = clientMessageService.createMessage(clientMessage);
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("user", message.getOwner());
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("room", message.getRoom());
        messagingTemplate.convertAndSendToUser(message.getMessageAbout().getLogin(), "/queue/reply",
                message.getRoom().getMessages());
        messagingTemplate.convertAndSend("/topic/" + roomName, message);
    }


}
