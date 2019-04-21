package lukianov.andrei.chat.controller;

import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.SimpleUser;
import lukianov.andrei.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @Autowired
    private Room room;

    @GetMapping("/")
    public String helloWorld() {
        int userId = room.getUsers().size();
        User user = new SimpleUser(userId, "user"+userId);
        room.getUsers().add(user);

        return "chat";
    }
}
