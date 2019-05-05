package lukianov.andrei.chat.controller;

import lombok.extern.slf4j.Slf4j;
import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class RoomController {


    @Autowired
    private RoomService roomService;

    @MessageMapping("/chat.senMessage")
    @SendTo("/topic/room")
    public Message sendMessage(@Payload Message message) {
        message.setDate(new Date());
        roomService.addMessageToRoom(message);
        return message;
    }

    @MessageMapping("/chat.addUserToRoom")
    @SendToUser("/queue/reply")
    public List<Message> addUserToRoom(@Payload User user) {
        log.info("owner connected " + user.getLogin());
        return roomService.getAllMessages();
    }

    @MessageMapping("/chat.notifyOtherUsers")
    @SendTo("/topic/room")
    public Message notifyUsersAboutNewConnect(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        if (roomService.addUserToRoom(user)) {
            log.info(user.getLogin() + " was added to room");
            Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("owner", user);
            return new Message(user, String.format("%s joined", user.getLogin()), new Date());
        }
        return new Message(user, "");
    }


}
