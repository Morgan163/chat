package lukianov.andrei.chat.commands.impl.room;

import lukianov.andrei.chat.commands.RoomCommand;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Message;


public class RoomCommandExecutor {

    public Message executeCommand(RoomCommand command) throws RoomCommandExecutionException {
        return command.execute();
    }
}
