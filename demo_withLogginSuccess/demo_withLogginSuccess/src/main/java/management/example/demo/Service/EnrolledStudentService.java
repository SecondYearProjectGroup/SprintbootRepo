package management.example.demo.Service;

import management.example.demo.Model.Student;
import management.example.demo.Model.User;
import management.example.demo.Repository.StudentRepository;
import management.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrolledStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;
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
        User user = new User(   user_.getFullName(),
                user_.getFullName(),
                user_.getFullName(),
                user_.getEmail(),
                passwordEncoder.encode(user_.getContactNumber()));
        return userRepository.save(user);
    }

}
