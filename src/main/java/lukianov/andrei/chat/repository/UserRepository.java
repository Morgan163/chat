package lukianov.andrei.chat.repository;

import lukianov.andrei.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByLogin(String login);
}
