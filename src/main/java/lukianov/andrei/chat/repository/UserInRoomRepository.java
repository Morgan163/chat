package lukianov.andrei.chat.repository;

import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.model.UserInRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserInRoomRepository extends JpaRepository<UserInRoom, Long> {
    UserInRoom findByUser(@Param("user") User user);

    UserInRoom findByUserAndRoom(@Param("user") User user, @Param("room") Room room);
}
