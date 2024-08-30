package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JoinColumn(name = "section_id")
    private ConfirmedStudentSections confirmedStudentSections;

    public Tile() {}

    public Tile(String type, String title, String routerLink) {
        this.type = type;
        this.title = title;
        this.routerLink = routerLink;
    }


    @OneToOne(mappedBy = "tile")
    @JsonManagedReference
    private Submission submission;

    // Getters and Setters
}