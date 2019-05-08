package lukianov.andrei.chat.commands;

import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Message;


public interface RoomCommand {
    Message execute() throws RoomCommandExecutionException;
}
