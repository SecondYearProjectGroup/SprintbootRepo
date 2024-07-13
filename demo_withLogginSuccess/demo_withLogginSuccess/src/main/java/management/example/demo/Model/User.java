package management.example.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String firstName;
    private String LastName;
    private String email;
    private String password;
    private String role;

    public User() {

    }

    public User(String username, String firstName,String LastName, String email, String password) {
        super();
        this.username = username;
        this.firstName = firstName;
        this.LastName = LastName;
        this.email = email;
        this.password = password;
        //this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", pwd='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
