package lukianov.andrei.chat.commands.impl.room;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.commands.RoomCommand;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Message;


@RequiredArgsConstructor
public class RemoveRoomCommand implements RoomCommand {
    private final RoomCommandReceiver commandReceiver;

    @Override
    public Message execute() throws RoomCommandExecutionException {
        return commandReceiver.removeRoom();
    }
}
