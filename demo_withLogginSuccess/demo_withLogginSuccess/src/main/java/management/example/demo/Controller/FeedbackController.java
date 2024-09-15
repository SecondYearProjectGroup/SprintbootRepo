package management.example.demo.Controller;

import management.example.demo.Model.Feedback;
import management.example.demo.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/submission/{submissionId}")
    public List<Feedback> getAllFeedbacksRelatedToSubmissionAndType(@PathVariable(name = "submissionId") Long submissionId){
        return feedbackService.getAllFeedbacks(submissionId);
    }
}