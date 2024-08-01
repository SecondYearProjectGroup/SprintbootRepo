package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    //Submissions
    @ManyToOne
    private ConfirmedStudent confirmedStudent;

    //Examiners who is assigned to submissions
    @ManyToMany
    @JoinTable(
            name = "submission_examiner",
            joinColumns = @JoinColumn(name = "submission_id"),
            inverseJoinColumns = @JoinColumn(name = "examiner_id")
    )
    @Getter
    private List<Examiner> examiners = new ArrayList<>();
}
