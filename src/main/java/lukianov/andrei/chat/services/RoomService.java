package lukianov.andrei.chat.services;

import lukianov.andrei.chat.model.Room;

import java.util.List;

public interface RoomService {
    Room addRoom(Room room);

    void delete(Room room);

    Room editRoom(Room room);

    List<Room> getAll();

    Room getRoomByName(String name);
}
