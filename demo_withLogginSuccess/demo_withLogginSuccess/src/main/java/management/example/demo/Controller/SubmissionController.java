package management.example.demo.Controller;

import management.example.demo.Model.Submission;
import management.example.demo.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/save-parameters")
    public Submission saveSubmission(@RequestBody Submission submission) {
        return submissionService.saveSubmissionsParameters(submission);
    }
}
