package lukianov.andrei.chat.controller;

import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.SimpleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final String LOGIN = "login";
    @Autowired
    private Room room;

    @GetMapping("/chat")
    public String loginForm(Model model) {
        model.addAttribute(LOGIN, "");
        return LOGIN;
    }

    @PostMapping("/chat")
    public String login(@RequestParam String login, Model model){
        room.getUsers().add(new SimpleUser(login));
        model.addAttribute(LOGIN, login);
        return "chat";
    }
}
