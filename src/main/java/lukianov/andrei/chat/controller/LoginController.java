package lukianov.andrei.chat.controller;

import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.SimpleUser;
import lukianov.andrei.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private Room room;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("login", "");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login, Model model){
        room.getUsers().add(new SimpleUser(login));
        model.addAttribute("login", login);
        return "chat";
    }
}
