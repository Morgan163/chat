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

import java.util.Date;

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
        message.setDate(new Date());
        message.setText(clientMessage.getContent());
        return messageService.addMessage(message);
    }

    public Message handleCommand(ClientMessage clientMessage) {
        String messageContent = clientMessage.getContent();
        if (!messageContent.startsWith("//")) {
            //TODO обработать ошибку
            return new Message();
        }
        User user = userService.getUserByLogin(clientMessage.getLogin());
        Room room = roomService.getRoomByName(clientMessage.getRoom());
        RoomCommandClient roomCommandClient = new RoomCommandClient(clientMessage.getContent(),
                user, room, roomService, userInRoomService, userService);
        RoomCommandExecutor roomCommandExecutor = new RoomCommandExecutor();
        try {
            return roomCommandExecutor.executeCommand(roomCommandClient.resolveRoomCommand());
        } catch (RoomCommandExecutionException ex) {
            Message message = new Message();
            message.setOwner(user);
            message.setDate(new Date());
            log.error(ex.getMessage());
            message.setText("ERROR " + ex.getMessage());
            message.setMessageType(MessageType.ERROR);
            return message;
        }
    }

    public Message messageWhenConnected(ClientMessage clientMessage){
        User user = userService.getUserByLogin(clientMessage.getLogin());
        Message message = new Message();
        message.setOwner(user);
        message.setMessageAbout(user);
        return message;
    }
}
