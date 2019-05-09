package lukianov.andrei.chat.services.impl;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.model.UserInRoom;
import lukianov.andrei.chat.repository.UserInRoomRepository;
import lukianov.andrei.chat.services.UserInRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserInRoomServiceImpl implements UserInRoomService {
    private final UserInRoomRepository userInRoomRepository;

    @Override
    public UserInRoom addUserInRoom(UserInRoom userInRoom) {
        return userInRoomRepository.saveAndFlush(userInRoom);
    }

    @Override
    public void delete(UserInRoom userInRoom) {
        userInRoomRepository.delete(userInRoom);
    }

    @Override
    public UserInRoom editUserInRoom(UserInRoom userInRoom) {
        return userInRoomRepository.saveAndFlush(userInRoom);
    }

    @Override
    public List<UserInRoom> getAll() {
        return userInRoomRepository.findAll();
    }

    @Override
    public UserInRoom findByUser(User user) {
        return userInRoomRepository.findByUser(user);
    }

    @Override
    public UserInRoom findByUserAndRoom(User user, Room room) {
        return userInRoomRepository.findByUserAndRoom(user, room);
    }
}
