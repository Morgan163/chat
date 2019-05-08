package lukianov.andrei.chat.commands;

import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Room;

import java.util.Optional;

public interface RoomCommand {
    Optional<Room> execute() throws RoomCommandExecutionException;
}
