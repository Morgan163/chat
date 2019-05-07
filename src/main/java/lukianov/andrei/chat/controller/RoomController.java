package lukianov.andrei.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.services.impl.RoomServiceImpl;
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

@RequiredArgsConstructor
@Slf4j
@Controller
public class RoomController {

    private final RoomServiceImpl roomServiceImpl;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/room")
    public Message sendMessage(@Payload Message message) {
        message.setDate(new Date());
        roomServiceImpl.addMessageToRoom(message);
        return message;
    }

    @MessageMapping("/chat.addUserToRoom")
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
    }


}
