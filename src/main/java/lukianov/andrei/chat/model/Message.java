package lukianov.andrei.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private User user;
    private String messageString;
    private Date messageDate;

    public Message(User user, String messageString) {
        this.user = user;
        this.messageString = messageString;
    }
}
