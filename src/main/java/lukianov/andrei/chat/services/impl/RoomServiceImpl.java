package lukianov.andrei.chat.services.impl;

import lukianov.andrei.chat.model.Message;
import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.repository.RoomRepository;
import lukianov.andrei.chat.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;


    private final Room room = new Room();

    public boolean addUserToRoom(User user) {
        return room.getUsers().add(user);
    }

    public boolean roomContainsUser(User user) {
        return room.getUsers().contains(user);
    }

    public boolean addMessageToRoom(Message message) {
        return room.getMessages().add(message);
    }

    public List<Message> getAllMessages() {
        return room.getMessages();
    }

    public void deleteUserFromRoom(User user) {
        room.getUsers().remove(user);
    }

    @Override
    public Room addRoom(Room room) {
        return roomRepository.saveAndFlush(room);
    }

    @Override
    public void delete(Room room) {
        roomRepository.delete(room);
    }

    @Override
    public Room editRoom(Room room) {
        return roomRepository.saveAndFlush(room);
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomByName(String name) {
        return roomRepository.findByName(name);
    }
}
