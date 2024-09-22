package management.example.demo.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameWithInitials;
    private String fullName;
    private String contactNumber;
    private String email;
    private String address;
//    private String university;
//    private String fromDate;
//    private String toDate;
//    private String degree;
//    private String field;
//    private String classPass;
    private String publications;
    private String programOfStudy;
    private String status;
    private String attachementFile;
    private String attachementFileOriginalName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileMetadata> fileMetadata;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EducationalQualification> educationalQualifications;
}