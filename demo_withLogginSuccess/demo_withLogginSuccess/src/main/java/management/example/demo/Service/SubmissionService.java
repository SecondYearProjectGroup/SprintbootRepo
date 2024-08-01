package management.example.demo.Service;

import management.example.demo.Model.Submission;
import management.example.demo.Repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService  {

    @Autowired
    private SubmissionRepository submissionRepository;

    public Submission get(long id){
        return submissionRepository.findById(id).get();
    }
}
