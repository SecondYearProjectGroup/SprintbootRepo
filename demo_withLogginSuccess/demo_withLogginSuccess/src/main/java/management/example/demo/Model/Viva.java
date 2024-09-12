package management.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Viva {

    @Id
    private Long id;
    private String title;
    private LocalDateTime vivaDate;


    //Vivas
    @ManyToOne
    @JoinColumn(name = "student_id")
    //@JsonBackReference("confirmedStudent-submissions")
    @JsonIgnore
    private ConfirmedStudent confirmedStudent;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tile_id")
    @JsonBackReference("viva-tile")
    private Tile tile;
}
