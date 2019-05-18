package lukianov.andrei.chat.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lukianov.andrei.chat.commands.impl.room.RoomCommandClient;
import lukianov.andrei.chat.commands.impl.room.RoomCommandExecutor;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.*;
import lukianov.andrei.chat.services.impl.MessageServiceImpl;
import lukianov.andrei.chat.services.impl.RoomServiceImpl;
import lukianov.andrei.chat.services.impl.UserInRoomServiceImpl;
import lukianov.andrei.chat.services.impl.UserServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientMessageService {
    private static final String ERROR_MESSAGE = "ERROR command didn't executed";

    private final UserServiceImpl userService;

    private final RoomServiceImpl roomService;

    private final MessageServiceImpl messageService;

    private final UserInRoomServiceImpl userInRoomService;


    public Message handleMessage(ClientMessage clientMessage) {
        Message message = new Message();
        message.setOwner(userService.getUserByLogin(clientMessage.getLogin()));
        message.setRoom(roomService.getRoomByName(clientMessage.getRoom()));
        message.setDate(LocalDateTime.now());
        message.setText(clientMessage.getContent());
        return messageService.addMessage(message);
    }

    public Message handleCommand(ClientMessage clientMessage) {
        String messageContent = clientMessage.getContent();
        User user = userService.getUserByLogin(clientMessage.getLogin());
        if (!messageContent.startsWith("//")) {
            return createErrorMessage(user, "Command not contains '//'");
        }
        Room room = roomService.getRoomByName(clientMessage.getRoom());
        RoomCommandClient roomCommandClient = new RoomCommandClient(clientMessage.getContent(),
                user, room, roomService, userInRoomService, userService);
        RoomCommandExecutor roomCommandExecutor = new RoomCommandExecutor();
        try {
            return roomCommandExecutor.executeCommand(roomCommandClient.resolveRoomCommand());
        } catch (RoomCommandExecutionException ex) {
            return createErrorMessage(user, ex.getMessage());
        }
    }
    private Message createErrorMessage(User user, String text){
        log.error(text);
        Message message = new Message();
        message.setOwner(user);
        message.setDate(LocalDateTime.now());
        message.setText("ERROR " + text);
        message.setMessageType(MessageType.ERROR);
        message.setMessageAbout(user);
        return message;
    }

    public Message messageWhenConnected(ClientMessage clientMessage) {
        User user = userService.getUserByLogin(clientMessage.getLogin());
        Message message = new Message();
        message.setOwner(user);
        message.setMessageAbout(user);
        return message;
    }

    public Message messageWhenOpenRoom(ClientMessage clientMessage) {
        Message message = messageWhenConnected(clientMessage);
        Room room = roomService.getRoomByName(clientMessage.getRoom());
        message.setRoom(room);
        message.setMessageType(MessageType.MESSAGE);
        return message;
    }
}
