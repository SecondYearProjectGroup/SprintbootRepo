package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Submission {

    @Id
    private Long id;

    private String title;
    private String fileName;
    private LocalDateTime openDate;
    private LocalDateTime deadline;

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
    private List<Forum> feedbacks;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tile_id")
    @JsonBackReference
    private Tile tile;
}
