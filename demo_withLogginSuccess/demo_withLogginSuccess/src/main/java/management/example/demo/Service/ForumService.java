package management.example.demo.Service;

import management.example.demo.Model.Forum;
import management.example.demo.Model.Submission;
import management.example.demo.Repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumService {
    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private SubmissionService submissionService;

    //To list all the feedbacks related to a submission of a student
    public List<Forum> getAllFeedbacks(Long submissionId){
        Submission submission= submissionService.get(submissionId);
        return forumRepository.findById(submission);
    }

    //Create a feedback
    public Forum addFeedback(Long submissionID, String body){
        Submission submission = submissionService.get(submissionID);
        Forum feedback = new Forum();
        feedback.setBody(body);
        feedback.setSubmission(submission);
        return forumRepository.save(feedback);
    }


    public Forum saveForum(Forum forum){
        return forumRepository.save(forum);
    }
}
