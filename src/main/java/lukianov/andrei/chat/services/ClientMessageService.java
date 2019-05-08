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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientMessageService {
    private static final String ERROR_MESSAGE = "ERROR command didn't executed";

    private final UserServiceImpl userService;

    private final RoomServiceImpl roomService;

    private final MessageServiceImpl messageService;

    private final UserInRoomServiceImpl userInRoomService;


    public Message createMessage(ClientMessage clientMessage) {
        String messageContent = clientMessage.getContent();
        if (messageContent.startsWith("//")) {
            return performCommand(clientMessage);
        }
        Message message = new Message();
        message.setOwner(userService.getUserByLogin(clientMessage.getUsername()));
        message.setRoom(roomService.getRoomByName(clientMessage.getRoom()));
        message.setDate(new Date());
        return messageService.addMessage(message);
    }

    private Message performCommand(ClientMessage clientMessage) {
        User user = userService.getUserByLogin(clientMessage.getUsername());
        Room room = roomService.getRoomByName(clientMessage.getRoom());
        RoomCommandClient roomCommandClient = new RoomCommandClient(clientMessage.getContent(),
                user, room, roomService, userInRoomService, userService);
        RoomCommandExecutor roomCommandExecutor = new RoomCommandExecutor();
        Optional<Room> roomOptional;
        Message message = new Message();
        message.setOwner(user);
        message.setDate(new Date());
        try {
            roomOptional = roomCommandExecutor.executeCommand(roomCommandClient.resolveRoomCommand());
        } catch (RoomCommandExecutionException ex) {
            log.error(ex.getMessage());
            message.setText(ERROR_MESSAGE);
            return message;
        }
        roomOptional.ifPresent(message::setRoom);
        message.setText(clientMessage.getContent());
        return message;
    }
}
