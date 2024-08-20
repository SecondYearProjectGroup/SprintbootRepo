package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class ConfirmedStudent {
    @Id
    private String regNumber;

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

    //This attribute is created for generating registration number
    @CreationTimestamp
    private LocalDate createdDate;

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



    //confirmed postgraduate student count in the year
    //this count will reset when the year changed
    @Getter
    @Transient //This attribute is not in the database table
    @GeneratedValue(strategy = GenerationType.AUTO)
    static int count;

    //year variable to compare with current year
    @Getter
    @Transient
    static int year = 2024;

    //Manage the count variable
    //Constructor
//    public ConfirmedStudent(){
//        count++;
//    }

    public static int setCountToOne() {
        return ConfirmedStudent.count = 1;
    }

}
