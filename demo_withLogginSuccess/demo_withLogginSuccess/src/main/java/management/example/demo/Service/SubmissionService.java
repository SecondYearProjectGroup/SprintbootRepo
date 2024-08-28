package management.example.demo.Service;

import management.example.demo.Model.Submission;
import management.example.demo.Repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SubmissionService  {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private FileService fileUploadService;

    public Submission get(Long id){
        return submissionRepository.findById(id).get();
    }

    //Save the submissions
    public void saveSubmissionsParameters(Submission submission){
        submissionRepository.save(submission);
    }

    //Upload the file (report)
    public void saveSubmission(Submission submission, @RequestParam("file")MultipartFile file){
        submission.setFileName(fileUploadService.uploadFile(file));
        submissionRepository.save(submission);
    }

    //Add a section to submit the reports by the admin
    public void addSubmissionField(Submission submission, @RequestParam String title){
        submission.setTitle(title);
        submissionRepository.save(submission);
    }
}
