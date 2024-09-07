package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Examiner {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    //private String nameWithInitials;
    private String fullName;
    private String department;
    private String email;

    @ManyToMany(mappedBy = "examiners")
    @JsonManagedReference
    private List<Submission> submissions;


}
