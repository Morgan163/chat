package lukianov.andrei.chat.controller;

import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RoomController {

    @Autowired
    private Room room;

    @MessageMapping("/chat.senMessage")
    @SendTo("/topic/room")
    public Message sendMessage(Message message) {
        room.getMessages().add(message);
        return message;
    }

    @MessageMapping("/chat.addUserToRoom")
    @SendToUser("/queue/reply")
    public List<Message> addUserToRoom(User user, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("user connected " + user.getName());
        List<Message> messages = room.getMessages();
        if (room.getUsers().add(user)) {
            headerAccessor.getSessionAttributes().put("user", user);
            messages.add(new Message(user, String.format("%s joined", user.getName())));
        }
        return messages;
    }

}
