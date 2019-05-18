package lukianov.andrei.chat.repository;

import lukianov.andrei.chat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {


    Room findByName(String name);
}
