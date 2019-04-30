package lukianov.andrei.chat.services;

import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private Room room;

    public boolean addUserToRoom(User user){
        return room.getUsers().add(user);
    }

    public boolean addMessageToRoom(Message message){
        return room.getMessages().add(message);
    }

    public List<Message> getAllMessages(){
        return room.getMessages();
    }

    public void deleteUserFromRoom(User user){
        room.getUsers().remove(user);
    }
}
