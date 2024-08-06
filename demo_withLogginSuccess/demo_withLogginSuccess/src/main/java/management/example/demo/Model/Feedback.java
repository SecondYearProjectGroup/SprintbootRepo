package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;
    //There should be an option to upload the marked report

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;
}
