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
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Controller
public class RoomController {
    public static final String QUEUE_REPLY = "/queue/reply";

    private final RoomServiceImpl roomServiceImpl;

    private final SimpMessageSendingOperations messagingTemplate;

    private final ClientMessageService clientMessageService;

    private final UserServiceImpl userService;


    @MessageMapping("/chat.connect")
    @SendToUser("/queue/reply")
    public Message connectMessage(@Payload ClientMessage clientMessage) {
        return clientMessageService.messageWhenConnected(clientMessage);
    }

    @MessageMapping("/chat.command")
    public void commandMessage(@Payload ClientMessage clientMessage) {
        Message message = clientMessageService.handleCommand(clientMessage);
        if (message.getMessageType().equals(MessageType.CONNECT) ||
                message.getMessageType().equals(MessageType.DISCONNECT)) {
            sendConnectOrCreate(message);
        } else if (message.getMessageType().equals(MessageType.REMOVE) ||
                message.getMessageType().equals(MessageType.RENAME)) {
            sendRemoveOrRename(message);
        } else if (message.getMessageType().equals(MessageType.CREATE)) {
            sendMessageToUserAbout(message);
        }
    }

    private void sendConnectOrCreate(Message message) {
        sendMessageToUserAbout(message);
        if (!message.getOwner().equals(message.getMessageAbout())) {
            message.setMessageAbout(message.getOwner());
            sendMessageToUserAbout(message);
        }
    }

    private void sendRemoveOrRename(Message message) {
        for (User user : message.getRoom().getUsers()) {
            message.setMessageAbout(user);
            sendMessageToUserAbout(message);
        }
    }

    private void sendMessageToUserAbout(Message message) {
        messagingTemplate.convertAndSendToUser(message.getMessageAbout().getLogin(), QUEUE_REPLY,
                message);
    }


    @MessageMapping("/chat/{roomName}/sendMessage")
    public void sendMessage(@DestinationVariable("roomName") String roomName, @Payload ClientMessage clientMessage) {
        Message message = clientMessageService.handleMessage(clientMessage);
        messagingTemplate.convertAndSend("/topic/" + roomName, message);
    }

    @MessageMapping("/chat/{roomName}/addUser")
    public void addUser(@DestinationVariable String roomName, @Payload ClientMessage clientMessage, SimpMessageHeaderAccessor headerAccessor) {
        Message message = clientMessageService.handleMessage(clientMessage);
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("user", message.getOwner());
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("room", message.getRoom());
        messagingTemplate.convertAndSendToUser(message.getMessageAbout().getLogin(), QUEUE_REPLY,
                message.getRoom().getMessages());
        messagingTemplate.convertAndSend("/topic/" + roomName, message);
    }


}
