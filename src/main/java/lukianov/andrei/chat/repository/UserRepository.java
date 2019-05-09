package lukianov.andrei.chat.repository;

import lukianov.andrei.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByLogin(@Param("login") String login);
}
