package lukianov.andrei.chat.controller;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.services.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/registrationPage")
    public String registration(@RequestParam String login, @RequestParam String password, Model model) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        if (Objects.nonNull(userService.getUserByLogin(login))) {
            model.addAttribute("error", "User already exists");
            return "registration";
        }
        userService.addUser(user);
        model.addAttribute("successful", "Sign up successfully");
        return "login";
    }

    @GetMapping("/registration")
    public String signUpForm(Model model) {
        model.addAttribute("error", "");
        return "registration";
    }
}
