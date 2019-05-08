package lukianov.andrei.chat.commands.impl.room;

import lukianov.andrei.chat.commands.RoomCommand;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Room;

import java.util.Optional;

public class RoomCommandExecutor {

    public Optional<Room> executeCommand(RoomCommand command) throws RoomCommandExecutionException {
        return command.execute();
    }
}
