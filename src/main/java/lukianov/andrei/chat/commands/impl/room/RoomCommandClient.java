package lukianov.andrei.chat.commands.impl.room;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.commands.Command;
import lukianov.andrei.chat.commands.TextCommands;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.services.RoomService;
import lukianov.andrei.chat.services.UserInRoomService;
import lukianov.andrei.chat.services.UserService;


@RequiredArgsConstructor
public class RoomCommandClient {
    private final String command;
    private final User userParameter;
    private final Room roomParameter;
    private final RoomService roomService;
    private final UserInRoomService userInRoomService;
    private final UserService userService;

    public Command resolveRoomCommand() throws RoomCommandExecutionException {
        RoomCommandReceiver roomCommandReceiver = new RoomCommandReceiver(command, userParameter, roomParameter,
                roomService, userInRoomService, userService);
        if (command.startsWith(TextCommands.ROOM_CONNECT)) {
            return new ConnectToRoomCommand(roomCommandReceiver);
        }
        if (command.startsWith(TextCommands.ROOM_CREATE)) {
            return new CreateRoomCommand(roomCommandReceiver);
        }
        if (command.startsWith(TextCommands.ROOM_DISCONNECT)) {
            return new DisconnectFromRoomCommand(roomCommandReceiver);
        }
        if (command.startsWith(TextCommands.ROOM_REMOVE)) {
            return new RemoveRoomCommand(roomCommandReceiver);
        }
        if (command.startsWith(TextCommands.ROOM_RENAME)) {
            return new RenameRoomCommand(roomCommandReceiver);
        }
        throw new RoomCommandExecutionException("Command is not exist");
    }
}
