package management.example.demo.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

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
    private String university;
    private String fromDate;
    private String toDate;
    private String degree;
    private String field;
    private String classPass;
    private String publications;
    private String programOfStudy;
    private String status;
    private String attachementFile;


}