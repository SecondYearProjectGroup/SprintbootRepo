package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class ConfirmedStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private String regNumber;


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

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    //Assign Supervisor to the student
    private Supervisor supervisor;



    //Student's submissions
    @OneToMany
    @JoinTable(
            name = "student_submission",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "submission_id")
    )
    @Getter
    private List<Submission> submissions;



    @Getter
    @Transient //This attribute is not in the database table
    static int count =0;

    //Manage the count variable
    //Constructor
    public ConfirmedStudent(){
        count++;
    }


}
