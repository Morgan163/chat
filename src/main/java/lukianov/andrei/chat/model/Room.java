package lukianov.andrei.chat.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room", schema = "public")
public class Room implements Serializable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonManagedReference
    private User owner;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(name = "users_in_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    //@OneToMany(mappedBy = "id", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "room", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Message> messages = new ArrayList<>();

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
        }
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }
}
