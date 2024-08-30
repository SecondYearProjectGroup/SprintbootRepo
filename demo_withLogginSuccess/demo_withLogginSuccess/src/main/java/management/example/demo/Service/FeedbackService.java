package management.example.demo.Service;

import management.example.demo.Model.Forum;
import management.example.demo.Model.Submission;
import management.example.demo.Repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SubmissionService submissionService;

    //To list all the feedbacks related to a submission of a student
    public List<Forum> getAllFeedbacks(Long submissionId){
        Submission submission= submissionService.get(submissionId);
        return feedbackRepository.findById(submission);
    }

    //Create a feedback
    public Forum addFeedback(Long submissionID, String body){
        Submission submission = submissionService.get(submissionID);
        Forum feedback = new Forum();
        feedback.setBody(body);
        feedback.setSubmission(submission);
        return feedbackRepository.save(feedback);
    }
}
