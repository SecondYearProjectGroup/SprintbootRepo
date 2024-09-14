package management.example.demo.Service;

import management.example.demo.DTO.StudentSubmissionExaminerDto;
import management.example.demo.Model.Submission;
import management.example.demo.Repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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


//    public List<StudentSubmissionExaminerDto> getAllStudentSubmissions() {
//        List<Object[]> results = submissionRepository.findAllStudentSubmissionDetailsRaw();
//        return results.stream()
//                .map(result -> new StudentSubmissionExaminerDto(
//                        (String) result[0], // regNumber
//                        (String) result[1], // registrationNumber
//                        (String) result[2], // nameWithInitials
//                        (String) result[3], // title
//                        (LocalDateTime) result[4], // deadline
//                        (Boolean) result[5], // submissionStatus
//                        Arrays.asList(((String) result[6]).split(", ")) // examiners
//                ))
//                .collect(Collectors.toList());
//    }

    // Service Method
    public List<StudentSubmissionExaminerDto> getAllStudentSubmissions() {
        List<Object[]> results = submissionRepository.findAllStudentSubmissionDetailsRaw();
        return results.stream()
                .map(result -> new StudentSubmissionExaminerDto(
                        (String) result[0], // regNumber
                        (String) result[1], // registrationNumber
                        (String) result[2], // nameWithInitials
                        (String) result[3], // title
                        convertToLocalDateTime((Timestamp) result[4]), // deadline
                        (Boolean) result[5], // submissionStatus
                        //convertToLocalDateTime((Timestamp) result[6]),
                        null,
                        Arrays.asList(((String) result[6]).split(", ")) // examiners
                ))
                .collect(Collectors.toList());
    }


    // Utility method to convert Timestamp to LocalDateTime
    private LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }


    public List<StudentSubmissionExaminerDto> getStudentSubmissionExaminerDetails(Long examinerId) {
        List<Object[]> results = submissionRepository.findStudentSubmissionExaminerDetailsByExaminerId(examinerId);
        List<StudentSubmissionExaminerDto> dtos = new ArrayList<>();

        for (Object[] result : results) {
            String regNumber = (String) result[0];
            String registrationNumber = (String) result[1];
            String nameWithInitials = (String) result[2];
            String title = (String) result[3];
            LocalDateTime deadline = (LocalDateTime) result[4];
            Boolean submissionStatus = (Boolean) result[5];
            LocalDateTime deadlineToReview = (LocalDateTime) result[6];
            //String examiners = (String) result[6]; // This will be a comma-separated list of examiner names

            // Convert comma-separated examiners list to a list if needed
            //List<String> examinerList = Arrays.asList(examiners.split(","));

            StudentSubmissionExaminerDto dto = new StudentSubmissionExaminerDto(
                    regNumber, registrationNumber, nameWithInitials, title, deadline, submissionStatus, deadlineToReview, null
            );
            dtos.add(dto);
        }
        return dtos;
    }

}
