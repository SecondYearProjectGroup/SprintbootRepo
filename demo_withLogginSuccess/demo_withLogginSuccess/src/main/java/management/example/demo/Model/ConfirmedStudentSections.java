package management.example.demo.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "confirm_student_sections")
public class ConfirmedStudentSections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regNumber;

    private String buttonName;

    @ElementCollection
    @CollectionTable(name = "section_tiles", joinColumns = @JoinColumn(name = "section_id"))
    private List<Tile> tiles;

    // Getters and Setters

    // Default Constructor
    public ConfirmedStudentSections() {}

    // Parameterized Constructor
    public ConfirmedStudentSections(String regNumber, String buttonName, List<Tile> tiles) {
        this.regNumber = regNumber;
        this.buttonName = buttonName;
        this.tiles = tiles;
    }


    @Embeddable
    class Tile {
        private String type;
        private String title;
        private String routerLink;

        // Getters and Setters

        // Default Constructor
        public Tile() {}

        // Parameterized Constructor
        public Tile(String type, String title, String routerLink) {
            this.type = type;
            this.title = title;
            this.routerLink = routerLink;
        }
    }
}
