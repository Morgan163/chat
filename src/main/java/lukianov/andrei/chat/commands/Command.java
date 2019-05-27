package lukianov.andrei.chat.commands;

import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Message;


public interface Command {
    Message execute() throws RoomCommandExecutionException;
}
