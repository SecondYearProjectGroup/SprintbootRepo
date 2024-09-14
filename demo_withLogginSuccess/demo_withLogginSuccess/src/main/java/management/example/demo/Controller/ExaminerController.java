package management.example.demo.Controller;

import management.example.demo.DTO.StudentSubmissionExaminerDto;
import management.example.demo.Model.Examiner;
import management.example.demo.Service.ExaminerService;
import management.example.demo.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExaminerController {

    @Autowired
    private ExaminerService examinerService;

    @Autowired
    private SubmissionService submissionService;

    @GetMapping("/getAssignedExaminers/{SubmissionId}")
    public List<Examiner> loadAssignedExaminers(@PathVariable(name = "SubmissionId") Long submissionId){
        return examinerService.findBySubmissionId(submissionId);
    }

    //Get examiner related submission details
    @GetMapping("/getAllSubmissions-examiner/{examinerId}")
    public List<StudentSubmissionExaminerDto> getAllSubmissionsExaminer(@PathVariable(name = "examinerId") Long examinerId){
        return submissionService.getStudentSubmissionExaminerDetails(examinerId);
    }
}
