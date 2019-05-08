package lukianov.andrei.chat.commands.impl;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.commands.RoomCommand;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Room;

import java.util.Optional;

@RequiredArgsConstructor
public class RemoveRoomCommand implements RoomCommand {
    private final RoomCommandReceiver commandReceiver;

    @Override
    public Optional<Room> execute() throws RoomCommandExecutionException {
        return commandReceiver.removeRoom();
    }
}
