package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;

    @Column(name = "`read`")
    private boolean read;


    //To have the relationship between the user and notification entities
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}