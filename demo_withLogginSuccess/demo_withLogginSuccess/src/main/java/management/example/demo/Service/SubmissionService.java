package management.example.demo.Service;

import management.example.demo.Model.Submission;
import management.example.demo.Repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SubmissionService  {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FileService fileUploadService;

    @Autowired
    private ExaminerService examinerService;

    public Submission get(Long id){
        return submissionRepository.findById(id).get();
    }

    public List<Submission> findAll(){
        return submissionRepository.findAll();
    }

    //Save the submissions
    public Submission saveSubmissionsParameters(Submission submission){
        return submissionRepository.save(submission);
    }
    //Upload the file (report)
    public void saveSubmission(Submission submission, @RequestParam("file")MultipartFile file){
        List<String> attachmentData = fileUploadService.uploadFile(file);
        submission.setFileName((String) attachmentData.get(1));
        submissionRepository.save(submission);
    }

    //Add a section to submit the reports by the admin
    public void addSubmissionField(Submission submission, @RequestParam String title){
        submission.setTitle(title);
        submissionRepository.save(submission);
    }

    public void deleteExaminerFromSubmission(Long submissionId, Long examinerId) {
        submissionRepository.removeExaminerFromSubmission(submissionId, examinerId);
    }

}
