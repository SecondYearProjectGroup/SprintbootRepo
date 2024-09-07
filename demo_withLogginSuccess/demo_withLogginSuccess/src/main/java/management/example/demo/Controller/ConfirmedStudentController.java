package management.example.demo.Controller;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.FileMetadata;
import management.example.demo.Model.Submission;
import management.example.demo.Service.ConfirmedStudentService;
import management.example.demo.Service.FileService;
import management.example.demo.Service.SubmissionService;
import management.example.demo.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ConfirmedStudentController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FileService fileService;

    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    @Autowired
    private SubmissionService submissionService;


    //To get the current logging student register number
    //For students - username is their reg number
    @GetMapping("/profile-student")
    public ConfirmedStudent getRegNumber(@RequestHeader ("Authorization") String token){
        String jwtToken = token.substring(7);
        String regNumber = jwtUtil.extractUsername(jwtToken);
        System.out.println(regNumber);
        return confirmedStudentService.get(regNumber);
    }

    //Upload the completed assignments
    @PostMapping("/complete-assignment/{tileId}")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files,
                                              @PathVariable(name = "tileId") Long tileId) {
        try {
            // Upload files and get the metadata
            List<FileMetadata> uploadResults = fileService.uploadFiles_(files);

            // Retrieve the existing submission
            Submission submission = submissionService.get(tileId);

            //Set the submission id to all the files
            for (FileMetadata fileMetadata: uploadResults){
                fileMetadata.setSubmission(submission);
            }

            if (submission == null) {
                return new ResponseEntity<>("Submission not found", HttpStatus.NOT_FOUND);
            }

            // Set file metadata to the submission
            submission.setFileMetadataList(uploadResults);
            //Set the submission status as true - Submitted
            submission.setSubmissionStatus(true);
            //Set the date and time as last update
            submission.setLastModified(LocalDateTime.now());

            // Save the updated submission
            submissionService.saveSubmissionsParameters(submission);

            return new ResponseEntity<>("Files uploaded and submission updated successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
