package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;
    //There should be an option to upload the marked report
    private String fileName;

    //To mark the pre or final report submission feedback
    private String type;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    @JsonIgnore
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "examiner_id")
    @JsonIgnore
    private Examiner examiner;

    @ManyToOne
    @JoinColumn(name = "student_id")
   // @JsonBackReference("confirmedStudent-forums")
    @JsonIgnore
    private ConfirmedStudent confirmedStudent;
}
