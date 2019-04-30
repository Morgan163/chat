package lukianov.andrei.chat.controller;

import lombok.extern.slf4j.Slf4j;
import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class RoomController {

    @Autowired
    private Room room;

    @MessageMapping("/chat.senMessage")
    @SendTo("/topic/room")
    public Message sendMessage(@Payload Message message) {
        message.setMessageDate(new Date());
        room.getMessages().add(message);
        return message;
    }

    @MessageMapping("/chat.addUserToRoom")
    @SendToUser("/queue/reply")
    public List<Message> addUserToRoom(@Payload User user) {
        log.info("user connected " + user.getName());
        return room.getMessages();
    }

    @MessageMapping("/chat.notifyOtherUsers")
    @SendTo("/topic/room")
    public Message notifyUsersAboutNewConnect(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        if (room.getUsers().add(user)) {
            headerAccessor.getSessionAttributes().put("user", user);
            return new Message(user, String.format("%s joined", user.getName()), new Date());
        }
        return new Message(user, "");
    }


}
