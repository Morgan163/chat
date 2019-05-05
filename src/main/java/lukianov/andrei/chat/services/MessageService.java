package lukianov.andrei.chat.services;

import lukianov.andrei.chat.model.Message;

import java.util.List;

public interface MessageService {
    Message addMessage(Message message);

    void delete(Message message);

    Message editMessage(Message message);

    List<Message> getAll();
}
