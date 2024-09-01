package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private String uploadDate;

    @ManyToOne
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")  // Foreign key column in FileMetadata table
    private Submission submission;
}
