package lukianov.andrei.chat.commands.impl.room;

import lombok.RequiredArgsConstructor;
import lukianov.andrei.chat.commands.TextCommands;
import lukianov.andrei.chat.exceptions.RoomCommandExecutionException;
import lukianov.andrei.chat.model.*;
import lukianov.andrei.chat.services.RoomService;
import lukianov.andrei.chat.services.UserInRoomService;
import lukianov.andrei.chat.services.UserService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
class RoomCommandReceiver {
    private static final String ROOM_NOT_SPECIFIED = "Room not found by specified name";
    private static final String SPACE_AND_WORD_REGEXP = "\\s(.*)";
    private static final String CREATE_ROOM_REGEXP = TextCommands.ROOM_CREATE + SPACE_AND_WORD_REGEXP;
    private static final String CREATE_PRIVATE_ROOM_REGEXP = TextCommands.ROOM_CREATE + "\\s-c" + SPACE_AND_WORD_REGEXP;
    private static final String REMOVE_ROOM_REGEXP = TextCommands.ROOM_REMOVE + SPACE_AND_WORD_REGEXP;
    private static final String CONNECT_TO_ROOM_REGEXP = TextCommands.ROOM_CONNECT + SPACE_AND_WORD_REGEXP;
    private static final String CONNECT_USER_TO_ROOM_REGEXP = TextCommands.ROOM_CONNECT + SPACE_AND_WORD_REGEXP
            + "\\s-l" + SPACE_AND_WORD_REGEXP;
    private static final String DISCONNECT_USER_FROM_ROOM_REGEXP = TextCommands.ROOM_DISCONNECT + "\\s-l"
            + SPACE_AND_WORD_REGEXP;
    private static final String DISCONNECT_FROM_ROOM_REGEXP = TextCommands.ROOM_DISCONNECT;
    private static final String RENAME_ROOM_REGEXP = TextCommands.ROOM_RENAME + SPACE_AND_WORD_REGEXP;


    private final String command;
    private final User userParameter;
    private final Room roomParameter;
    private final RoomService roomService;
    private final UserInRoomService userInRoomService;
    private final UserService userService;

    Message createRoom() throws RoomCommandExecutionException {
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

    private Message createPublicRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        room.setOwner(userParameter);
        room = roomService.addRoom(room);
        UserInRoom userInRoom = new UserInRoom();
        userInRoom.setRoom(room);
        userInRoom.setUser(userParameter);
        userInRoomService.addUserInRoom(userInRoom);
        User updatedUser = userService.getUserByLogin(userParameter.getLogin());
        return createMessage(room, updatedUser, String.format("room %s was created", roomName),
                updatedUser, MessageType.CREATE);
    }

    private Message createPrivateRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        room.setOwner(userParameter);
        room.setPrivate(true);
        room = roomService.addRoom(room);
        UserInRoom userInRoom = new UserInRoom();
        userInRoom.setRoom(room);
        userInRoom.setUser(userParameter);
        userInRoomService.addUserInRoom(userInRoom);
        User updatedUser = userService.getUserByLogin(userParameter.getLogin());
        return createMessage(room, updatedUser, String.format("room %s was created", roomName),
                updatedUser, MessageType.CREATE);
    }

    Message removeRoom() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(REMOVE_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return removeRoom(matcher.group(1));
        }
        throw new RoomCommandExecutionException("Room name not specify");
    }

    private Message removeRoom(String roomName) throws RoomCommandExecutionException {
        Room room = roomService.getRoomByName(roomName);
        if (Objects.isNull(room)) {
            throw new RoomCommandExecutionException(ROOM_NOT_SPECIFIED);
        }

        roomService.delete(room);
        User updatedUser = userService.getUserByLogin(userParameter.getLogin());
        return createMessage(room, updatedUser, String.format("room %s was deleted", roomName),
                updatedUser, MessageType.REMOVE);
    }

    Message connectToRoom() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(CONNECT_USER_TO_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return connectToRoom(matcher.group(1), matcher.group(2));
        }
        matcher = Pattern.compile(CONNECT_TO_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return connectToRoom(matcher.group(1));
        }
        throw new RoomCommandExecutionException("Room name or userParameter login not specify");
    }

    private Message connectToRoom(String roomName) throws RoomCommandExecutionException {
        Room room = roomService.getRoomByName(roomName);
        if (Objects.isNull(room)) {
            throw new RoomCommandExecutionException(ROOM_NOT_SPECIFIED);
        }
        UserInRoom userInRoom = new UserInRoom();
        userInRoom.setUser(userParameter);
        userInRoom.setRoom(room);
        userInRoomService.addUserInRoom(userInRoom);
        User updatedUser = userService.getUserByLogin(userParameter.getLogin());
        return createMessage(room, updatedUser, String.format("user %s was joined", userParameter.getLogin()),
                updatedUser, MessageType.CONNECT);
    }

    private Message connectToRoom(String roomName, String userLogin) throws RoomCommandExecutionException {
        Room room = roomService.getRoomByName(roomName);
        if (Objects.isNull(room)) {
            throw new RoomCommandExecutionException(ROOM_NOT_SPECIFIED);
        }
        User specifiedUser = userService.getUserByLogin(userLogin);
        if (Objects.isNull(specifiedUser)) {
            throw new RoomCommandExecutionException("User not found by specified login");
        }
        UserInRoom userInRoom = new UserInRoom();
        userInRoom.setUser(specifiedUser);
        userInRoom.setRoom(room);
        userInRoomService.addUserInRoom(userInRoom);
        specifiedUser = userService.getUserByLogin(specifiedUser.getLogin());
        return createMessage(room, specifiedUser, String.format("user %s was joined", specifiedUser.getLogin()),
                specifiedUser, MessageType.CONNECT);
    }

    Message disconnect() throws RoomCommandExecutionException {
        Matcher matcher = Pattern.compile(DISCONNECT_USER_FROM_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            return disconnect(matcher.group(1));
        }
        matcher = Pattern.compile(DISCONNECT_FROM_ROOM_REGEXP).matcher(command);
        if (matcher.find()) {
            UserInRoom userInRoom = userInRoomService.findByUserAndRoom(userParameter, roomParameter);
            if (Objects.isNull(userInRoom)) {
                throw new RoomCommandExecutionException("User is not in this room");
            }
            userInRoomService.delete(userInRoom);
            User updatedUser = userService.getUserByLogin(userParameter.getLogin());
            return createMessage(roomParameter, updatedUser, String.format("user %s was disconnected",
                    userParameter.getLogin()), updatedUser, MessageType.DISCONNECT);
        }
        throw new RoomCommandExecutionException("Command is not correct");
    }

    private Message disconnect(String userLogin) throws RoomCommandExecutionException {
        if (roomParameter.getOwner().equals(userParameter) || userParameter.getRole().equals(Role.ADMINISTRATOR)) {
            User specifiedUser = userService.getUserByLogin(userLogin);
            if (Objects.isNull(specifiedUser)) {
                throw new RoomCommandExecutionException("User not found by specified login");
            }
            UserInRoom userInRoom = userInRoomService.findByUserAndRoom(specifiedUser, roomParameter);
            if (Objects.isNull(userInRoom)) {
                throw new RoomCommandExecutionException("User is not in this roomParameter");
            }
            userInRoomService.delete(userInRoom);
            specifiedUser = userService.getUserByLogin(specifiedUser.getLogin());
            return createMessage(roomParameter, userParameter, String.format("user %s was disconnected",
                    specifiedUser.getLogin()), specifiedUser, MessageType.DISCONNECT);
        }
        throw new RoomCommandExecutionException("You have not permission");
    }

    Message rename() throws RoomCommandExecutionException {
        if (roomParameter.getOwner().equals(userParameter) || userParameter.getRole().equals(Role.ADMINISTRATOR)) {
            Matcher matcher = Pattern.compile(RENAME_ROOM_REGEXP).matcher(command);
            if (matcher.find()) {
                return rename(matcher.group(1));
            }
            throw new RoomCommandExecutionException("New room name not specify");
        }
        throw new RoomCommandExecutionException("You have not permission");
    }

    private Message rename(String roomName) {
        roomParameter.setName(roomName);
        Room updatedRoom = roomService.editRoom(roomParameter);
        User updatedUser = userService.getUserByLogin(userParameter.getLogin());
        return createMessage(updatedRoom, updatedUser, String.format("user %s renamed room to %s",
                userParameter.getLogin(), roomName), updatedUser, MessageType.RENAME);
    }

    private Message createMessage(Room room, User user, String text, User messageAbout, MessageType messageType) {
        Message message = new Message();
        message.setRoom(room);
        message.setOwner(user);
        message.setDate(LocalDateTime.now());
        message.setText(text);
        message.setMessageAbout(messageAbout);
        message.setMessageType(messageType);
        return message;
    }

}
