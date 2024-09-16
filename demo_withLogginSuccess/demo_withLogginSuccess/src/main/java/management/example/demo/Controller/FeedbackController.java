package management.example.demo.Controller;

import management.example.demo.Model.Feedback;
import management.example.demo.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    // Update feedback with body and file
    @PutMapping("/submission/{submissionId}/examiner/{examinerId}")
    public ResponseEntity<Feedback> updateFeedback(
            @PathVariable Long submissionId,
            @PathVariable Long examinerId,
            @RequestParam("body") String body,
            @RequestParam("file") MultipartFile file) throws IOException {

        Feedback updatedFeedback = feedbackService.updateFeedback(submissionId, examinerId, body, file);
        return ResponseEntity.ok(updatedFeedback);
    }
}