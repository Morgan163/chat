package lukianov.andrei.chat.services.impl;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.repository.MessageRepository;
import lukianov.andrei.chat.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;


    @Override
    public Message addMessage(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public Message editMessage(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }
}
