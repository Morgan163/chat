package lukianov.andrei.chat.model;

import lombok.Data;

@Data
public class ClientMessage {
    private final String content;

    private final String login;

    private final String room;
}
