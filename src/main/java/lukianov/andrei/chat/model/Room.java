package lukianov.andrei.chat.model;

import lombok.Data;

import java.util.*;

@Data
public class Room {
    private int id;
    private Set<User> users = new HashSet<>();
    private List<Message> messages = new LinkedList<>();
}
