package lukianov.andrei.chat.model;

import lombok.Data;

@Data
public class SimpleUser extends User {

    public SimpleUser(String name) {
        super(name);
    }
}
