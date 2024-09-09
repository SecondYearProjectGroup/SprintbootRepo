package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "confirm_student_sections")
@Getter
@Setter
public class ConfirmedStudentSections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regNumber;
    private String buttonName;
    private String activeTab;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)  // Add cascade here
    @JoinTable(
            name = "section_tiles",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "tile_id")
    )
    @Getter
    private List<Tile> tiles;
    // Getters and Setters

    public ConfirmedStudentSections() {}

    public ConfirmedStudentSections(String regNumber, String buttonName, List<Tile> tiles) {
        this.regNumber = regNumber;
        this.buttonName = buttonName;
        this.tiles = tiles;
    }

    // Getters and Setters
}