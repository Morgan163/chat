package lukianov.andrei.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final String LOGIN = "login";

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute(LOGIN, "");
        return LOGIN;
    }

    @GetMapping("/chat")
    public String login(Principal principal, Model model) {
        model.addAttribute(LOGIN, principal.getName());
        return "chat";
    }

}
