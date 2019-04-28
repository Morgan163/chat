package lukianov.andrei.chat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleUser extends User {
    public SimpleUser(String name) {
        super(name);
    }
}
