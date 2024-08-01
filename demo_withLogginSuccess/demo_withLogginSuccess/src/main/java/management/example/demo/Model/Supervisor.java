package management.example.demo.Model;

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

    private String nameWithInitials;
    private String fullName;
    private String department;
    private String email;
    private int noOfSupervisees;

    //There should be several supervisees in a table. How handle it???
    //Option 01: Join table with the confirmed student table.
    @OneToMany(mappedBy = "supervisor")
    private List<ConfirmedStudent> supervisees;

}
