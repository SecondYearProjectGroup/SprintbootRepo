package management.example.demo.Service;

import management.example.demo.Model.Feedback;
import management.example.demo.Model.Submission;
import management.example.demo.Repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private SubmissionService submissionService;

    //To list all the feedbacks related to a submission of a student (Student, Admin, Supervisor)
    public List<Feedback> getAllFeedbacks(Long submissionId){
        return feedbackRepository.findAllBySubmissionId(submissionId);
    }

    //Create a feedback
    public Feedback addFeedback(Long submissionID, String body){
        Submission submission = submissionService.get(submissionID);
        Feedback feedback = new Feedback();
        feedback.setBody(body);
        feedback.setSubmission(submission);
        return feedbackRepository.save(feedback);
    }


    public Feedback saveForum(Feedback feedback){
        return feedbackRepository.save(feedback);
    }


    //To load the feedback component to the examiners
    public List<Feedback> getFilteredFeedback(Long submissionId, Long examinerId, String type) {
        List<Feedback> feedbackList = feedbackRepository.findAllBySubmissionId(submissionId);

        // Filter feedback based on examiner and type
        return feedbackList.stream()
                .filter(feedback -> feedback.getExaminer().getId().equals(examinerId) && feedback.getType().equals(type))
                .collect(Collectors.toList());
    }

}
