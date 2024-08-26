package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Supervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String nameWithInitials;
    private String fullName;
    //private String department;
    private String email;
    private int noOfSupervisees;

    //There should be several supervisees in a table. How handle it???
    //Option 01: Join table with the confirmed student table.
    @OneToMany
    @JoinTable(
            name = "supervisor_supervisees",
            joinColumns = @JoinColumn(name = "supervisor_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore
    private List<ConfirmedStudent> supervisees;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Getters and setters

}
