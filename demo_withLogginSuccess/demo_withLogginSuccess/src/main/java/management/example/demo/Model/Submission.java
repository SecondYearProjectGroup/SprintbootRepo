package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String fileName;
    private Date deadline;

    //Submissions
    @ManyToOne
    @JoinColumn(name = "student_id")
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

    //To have the relationship with the feedback entity
    @OneToMany(mappedBy = "submission" , cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;
}
