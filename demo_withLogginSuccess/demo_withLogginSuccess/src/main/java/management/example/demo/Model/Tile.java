package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Tile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String title;
    private String routerLink;
    private String regNumber;

    @ManyToOne
    //@JsonBackReference("tiles_confirmedStudentSections")
    //@JsonIgnore
    private ConfirmedStudentSections confirmedStudentSections;

    public Tile() {}

    public Tile(String type, String title, String routerLink) {
        this.type = type;
        this.title = title;
        this.routerLink = routerLink;
    }


    @OneToOne(mappedBy = "tile")
    //@JsonManagedReference("tile-submission")
    @JsonIgnore
    private Submission submission;


    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private ConfirmedStudent confirmedStudent;
    // Getters and Setters
}