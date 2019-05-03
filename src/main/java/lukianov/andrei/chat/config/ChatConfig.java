package lukianov.andrei.chat.config;

import lukianov.andrei.chat.model.Room;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public Room room(){
        return new Room();
    }
}