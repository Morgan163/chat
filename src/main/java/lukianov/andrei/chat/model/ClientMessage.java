package lukianov.andrei.chat.model;

import lombok.Data;

@Data
public class ClientMessage {
    private final String content;

    private final String username;

    private final String room;
}
