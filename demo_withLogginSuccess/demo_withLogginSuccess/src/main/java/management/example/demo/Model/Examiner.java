package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Examiner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String nameWithInitials;
    private String fullName;
    private String department;
    private String email;

    @ManyToMany(mappedBy = "examiners")
    private List<Submission> submissions;
}
