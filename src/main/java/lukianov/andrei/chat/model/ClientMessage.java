package lukianov.andrei.chat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientMessage {
    private String content;

    private String login;

    private String room;
}
