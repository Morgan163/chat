package lukianov.andrei.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private final int id;
    private final String name;
}
