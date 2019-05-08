package lukianov.andrei.chat.services;


import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.model.UserInRoom;

import java.util.List;

public interface UserInRoomService {
    UserInRoom addUserInRoom(UserInRoom userInRoom);

    void delete(UserInRoom userInRoom);

    UserInRoom editUserInRoom(UserInRoom userInRoom);

    List<UserInRoom> getAll();

    UserInRoom findByUser(User user);

    UserInRoom findByUserAndRoom(User user, Room room);
}
