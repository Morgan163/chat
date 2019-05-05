package lukianov.andrei.chat.repository;

import lukianov.andrei.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository  extends JpaRepository<Message, Long> {
}
