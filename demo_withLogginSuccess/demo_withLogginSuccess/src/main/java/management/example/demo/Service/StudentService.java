package management.example.demo.Service;

import management.example.demo.Model.Student;
import management.example.demo.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;



    public Student saveStudent(Student student) {
        //student.setStatus(Student.Status.valueOf("PENDING"));
        return studentRepository.save(student);
    }
}











