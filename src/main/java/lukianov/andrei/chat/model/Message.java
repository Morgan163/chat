package lukianov.andrei.chat.model;

import lombok.Data;

@Data
public class Message {
    private final User user;
    private final String messageString;
}
