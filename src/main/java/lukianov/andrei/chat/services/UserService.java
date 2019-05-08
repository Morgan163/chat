package lukianov.andrei.chat.services;

import lukianov.andrei.chat.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    void delete(User user);

    User editUser(User user);

    List<User> getAll();

    User getUserByLogin(String login);
}
