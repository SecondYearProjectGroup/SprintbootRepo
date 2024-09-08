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
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;
    //There should be an option to upload the marked report

    @ManyToOne
    @JoinColumn(name = "submission_id")
    @JsonIgnore
    private Submission submission;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tile_id")
    //@JsonBackReference
    @JsonIgnore
    private Tile tile;

    @ManyToOne
    @JoinColumn(name = "student_id")
   // @JsonBackReference("confirmedStudent-forums")
    @JsonIgnore
    private ConfirmedStudent confirmedStudent;
}
