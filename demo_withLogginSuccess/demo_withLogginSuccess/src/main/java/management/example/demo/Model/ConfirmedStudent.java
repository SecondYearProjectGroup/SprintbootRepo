package management.example.demo.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
public class ConfirmedStudent {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String regNumber;
    //private Long id;


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




    @Getter
    @Transient //This attribute is not in the database table
    static int count =0;

    //Manage the count variable
    //Constructor
    public ConfirmedStudent(){
        count++;
    }


}
