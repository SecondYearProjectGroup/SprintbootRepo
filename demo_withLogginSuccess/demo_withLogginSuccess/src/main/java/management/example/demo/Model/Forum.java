package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;
    //There should be an option to upload the marked report

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tile_id")
    @JsonBackReference
    private Tile tile;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private ConfirmedStudent confirmedStudent;
}