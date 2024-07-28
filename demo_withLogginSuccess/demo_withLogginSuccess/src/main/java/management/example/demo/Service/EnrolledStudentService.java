package management.example.demo.Service;


import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.Student;
import management.example.demo.Model.User;
import management.example.demo.Repository.ConfirmedStudentRepository;
import management.example.demo.Repository.StudentRepository;
import management.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrolledStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmedStudentRepository confirmedStudentRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    public Student get(long id){
        return studentRepository.findById(id).get();
    }

    public void delete(long id){
        studentRepository.deleteById(id);
    }

    public User confirm(Student user_){
        User user = new User(
                //Assuming fullName as the username, firstName, and LastName
                user_.getFullName(),
                user_.getFullName(),
                user_.getFullName(),
                //Get the email
                user_.getEmail(),
                //Assuming contactNumber as the password
                passwordEncoder.encode(user_.getContactNumber()));
        return userRepository.save(user); //Save user in the database
    }


    //Save the student details in the confirmed student table
    public void saveStudent(Student user_){
        ConfirmedStudent confirmedStudent = new ConfirmedStudent();
        //confirmedStudent.setId(user_.getId());
        //Generate the registration number
        confirmedStudent.setRegNumber("PG/"+ LocalDate.now().getYear() + "/" + String.valueOf(ConfirmedStudent.getCount()));
        confirmedStudent.setNameWithInitials(user_.getNameWithInitials());
        confirmedStudent.setFullName(user_.getFullName());
        confirmedStudent.setEmail(user_.getEmail());
        confirmedStudent.setAddress(user_.getAddress());
        confirmedStudent.setUniversity(user_.getUniversity());
        confirmedStudent.setFromDate(user_.getFromDate());
        confirmedStudent.setToDate(user_.getToDate());
        confirmedStudent.setDegree(user_.getDegree());
        confirmedStudent.setField(user_.getField());
        confirmedStudent.setClassPass(user_.getClassPass());
        confirmedStudent.setPublications(user_.getPublications());
        confirmedStudent.setProgramOfStudy(user_.getProgramOfStudy());
        confirmedStudent.setStatus(user_.getStatus());
        confirmedStudentRepository.save(confirmedStudent);
    }



}
