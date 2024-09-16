package management.example.demo.Service;

import management.example.demo.Model.Feedback;
import management.example.demo.Model.Submission;
import management.example.demo.Repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private FileService fileService;

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
    

//    To update the feedback
    public Feedback updateFeedback(Long submissionId, Long examinerId, String body, MultipartFile file) throws IOException {
        Optional<Feedback> feedbackOpt = feedbackRepository.findBySubmissionIdAndExaminerId(submissionId, examinerId);
        if (feedbackOpt.isPresent()){
            Feedback feedback = feedbackOpt.get();
            feedback.setBody(body);
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                feedback.setFileName(fileName);
                //To handle the fileMetadata
                fileService.uploadFile(file);
            }
            return feedbackRepository.save(feedback);
        } else {
            throw new IllegalArgumentException("Feedback not found");
        }
    }


}
