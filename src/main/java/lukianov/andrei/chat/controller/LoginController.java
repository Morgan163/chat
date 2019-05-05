package lukianov.andrei.chat.controller;

import lukianov.andrei.chat.services.impl.RoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final String LOGIN = "login";
    @Autowired
    private RoomServiceImpl roomServiceImpl;

    @GetMapping("/chat")
    public String loginForm(Model model) {
        model.addAttribute(LOGIN, "");
        return LOGIN;
    }

    @PostMapping("/chat")
    public String login(@RequestParam String login, Model model){
        model.addAttribute(LOGIN, login);
        return "chat";
    }
}
