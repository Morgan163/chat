package lukianov.andrei.chat.model;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private final User user;
    private final String messageString;
    private Date messageDate;

    public Message(User user, String messageString) {
        this.user = user;
        this.messageString = messageString;
    }
}
