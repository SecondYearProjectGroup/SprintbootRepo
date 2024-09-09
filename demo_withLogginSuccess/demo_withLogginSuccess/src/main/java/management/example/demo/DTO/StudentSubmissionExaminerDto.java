package management.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentSubmissionExaminerDto {
    private String regNumber;
    private String registrationNumber;
    private String nameWithInitials;
    private String title;
    private List<String> examiners;

    // Constructors, Getters, and Setters
    public StudentSubmissionExaminerDto(String regNumber, String registrationNumber, String nameWithInitials, String title, List<String> examiners) {
        this.regNumber = regNumber;
        this.registrationNumber = registrationNumber;
        this.nameWithInitials = nameWithInitials;
        this.title = title;
        this.examiners = examiners;
    }


    public StudentSubmissionExaminerDto() {

    }

    // Getters and Setters
}