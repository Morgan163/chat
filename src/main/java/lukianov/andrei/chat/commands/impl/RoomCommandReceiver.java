package lukianov.andrei.chat.commands.impl;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.commands.TextCommands;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.Room;
import lukianov.andrei.chat.model.User;
import lukianov.andrei.chat.model.UserInRoom;
import lukianov.andrei.chat.services.RoomService;
import lukianov.andrei.chat.services.UserInRoomService;
import lukianov.andrei.chat.services.UserService;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RoomCommandReceiver {
    private static final String SPACE_AND_WORD_REGEXP = "\\s(.*)";
    private static final String CREATE_ROOM_REGEXP = TextCommands.ROOM_CREATE + SPACE_AND_WORD_REGEXP;
    private static final String CREATE_PRIVATE_ROOM_REGEXP = TextCommands.ROOM_CREATE + "\\s-c" + SPACE_AND_WORD_REGEXP;
    private static final String REMOVE_ROOM_REGEXP = TextCommands.ROOM_REMOVE + SPACE_AND_WORD_REGEXP;
    private static final String CONNECT_TO_ROOM_REGEXP = TextCommands.ROOM_CONNECT + SPACE_AND_WORD_REGEXP;
    private static final String CONNECT_USER_TO_ROOM_REGEXP = TextCommands.ROOM_CONNECT + SPACE_AND_WORD_REGEXP
            + "\\s-l" + SPACE_AND_WORD_REGEXP;
    private static final String DISCONNECT_USER_FROM_ROOM_REGEXP = TextCommands.ROOM_DISCONNECT + "\\s-l"
            + SPACE_AND_WORD_REGEXP;
    private static final String RENAME_ROOM_REGEXP = TextCommands.ROOM_RENAME + SPACE_AND_WORD_REGEXP;


    private final String command;
    private final User userParameter;
    private final Room roomParameter;
    private final RoomService roomService;
    private final UserInRoomService userInRoomService;
    private final UserService userService;

    Optional<Room> createRoom() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(CREATE_PRIVATE_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return createPrivateRoom(matcher.group(1));
        }
        matcher = Pattern.compile(CREATE_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return createPublicRoom(matcher.group(1));
        }
        throw new RoomCommandExecutionException("Room name not specify");
    }

    private Optional<Room> createPublicRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        room.setOwner(userParameter);
        return Optional.of(roomService.addRoom(room));
    }

    private Optional<Room> createPrivateRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        room.setOwner(userParameter);
        room.setPrivate(true);
        return Optional.of(roomService.addRoom(room));
    }

    Optional<Room> removeRoom() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(REMOVE_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return removeRoom(matcher.group(1));
        }
        throw new RoomCommandExecutionException("Room name not specify");
    }

    private Optional<Room> removeRoom(String roomName) throws RoomCommandExecutionException {
        Room room = roomService.getRoomByName(roomName);
        if (Objects.isNull(room)) {
            throw new RoomCommandExecutionException("Room not found by specified name");
        }
        roomService.delete(room);
        return Optional.empty();
    }

    Optional<Room> connectToRoom() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(CONNECT_USER_TO_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return connectToRoom(matcher.group(1));
        }
        matcher = Pattern.compile(CONNECT_TO_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return connectToRoom(matcher.group(1), matcher.group(2));
        }
        throw new RoomCommandExecutionException("Room name or userParameter login not specify");
    }

    private Optional<Room> connectToRoom(String roomName) throws RoomCommandExecutionException {
        Room room = roomService.getRoomByName(roomName);
        if (Objects.isNull(room)) {
            throw new RoomCommandExecutionException("Room not found by specified name");
        }
        UserInRoom userInRoom = new UserInRoom();
        userInRoom.setUser(userParameter);
        userInRoom.setRoom(room);
        userInRoomService.addUserInRoom(userInRoom);
        return Optional.of(room);
    }

    private Optional<Room> connectToRoom(String roomName, String userLogin) throws RoomCommandExecutionException {
        Room room = roomService.getRoomByName(roomName);
        if (Objects.isNull(room)) {
            throw new RoomCommandExecutionException("Room not found by specified name");
        }
        User specifiedUser = userService.getUserByLogin(userLogin);
        if (Objects.isNull(specifiedUser)) {
            throw new RoomCommandExecutionException("User not found by specified login");
        }
        UserInRoom userInRoom = new UserInRoom();
        userInRoom.setUser(specifiedUser);
        userInRoom.setRoom(room);
        userInRoomService.addUserInRoom(userInRoom);
        return Optional.of(room);
    }

    Optional<Room> disconnect() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(DISCONNECT_USER_FROM_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return disconnect(matcher.group(1));
        }
        matcher = Pattern.compile(CONNECT_TO_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            UserInRoom userInRoom = userInRoomService.findByUserAndRoom(userParameter, roomParameter);
            if (Objects.isNull(userInRoom)) {
                throw new RoomCommandExecutionException("User is not in this roomParameter");
            }
            userInRoomService.delete(userInRoom);
            return Optional.of(roomParameter);
        }
        throw new RoomCommandExecutionException("Command is not correct");
    }

    private Optional<Room> disconnect(String userLogin) throws RoomCommandExecutionException {
        User specifiedUser = userService.getUserByLogin(userLogin);
        if (Objects.isNull(specifiedUser)) {
            throw new RoomCommandExecutionException("User not found by specified login");
        }
        UserInRoom userInRoom = userInRoomService.findByUserAndRoom(specifiedUser, roomParameter);
        if (Objects.isNull(userInRoom)) {
            throw new RoomCommandExecutionException("User is not in this roomParameter");
        }
        userInRoomService.delete(userInRoom);
        return Optional.of(roomParameter);
    }

    Optional<Room> rename() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(RENAME_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return rename(matcher.group(1));
        }
        throw new RoomCommandExecutionException("New room name not specify");
    }

    private Optional<Room> rename(String roomName) {
        roomParameter.setName(roomName);
        return Optional.of(roomService.editRoom(roomParameter));
    }

}
