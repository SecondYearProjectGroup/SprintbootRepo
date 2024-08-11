package management.example.demo.Service;

import management.example.demo.Model.Examiner;
import management.example.demo.Repository.ExaminerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExaminerService {

    @Autowired
    private ExaminerRepository examinerRepository;

    public List<Examiner> listAll(){
        return examinerRepository.findAll();
    }
}
