package lukianov.andrei.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id = 0;
    private String name;

    public User(String name){

    }
}
