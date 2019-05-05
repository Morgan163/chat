package lukianov.andrei.chat.repository;

import lukianov.andrei.chat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from room r where r.name = :name")
    Room findByName(@Param("name") String name);
}
