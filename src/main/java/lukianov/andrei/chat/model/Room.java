package lukianov.andrei.chat.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
public class Room {
    private List<User> users = new ArrayList<>();
    private Queue<String> messages = new LinkedList<>();
}
