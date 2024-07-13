package management.example.demo.Service;

import management.example.demo.Model.Student;
import management.example.demo.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrolledStudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    public Student get(long id){
        return studentRepository.findById(id).get();
    }

    public void delete(long id){
        studentRepository.deleteById(id);
    }
}
