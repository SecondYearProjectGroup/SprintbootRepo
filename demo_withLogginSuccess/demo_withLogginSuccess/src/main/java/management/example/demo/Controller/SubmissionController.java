package management.example.demo.Controller;

import management.example.demo.Model.Submission;
import management.example.demo.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import management.example.demo.DTO.SubmissionDetailDto;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/save-parameters")
    public Submission saveSubmission(@RequestBody Submission submission) {
        return submissionService.saveSubmissionsParameters(submission);
    }


    @GetMapping("/getSubmissionDetails/{tileId}")
    public ResponseEntity<SubmissionDetailDto> getSubmissionDetails(@PathVariable(name = "tileId") Long tileId) {
        Submission submission = submissionService.get(tileId);
        if (submission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Create a DTO to send only the required data
        SubmissionDetailDto dto = new SubmissionDetailDto();
        dto.setOpenedDate(submission.getOpenDate());
        dto.setDueDate(submission.getDeadline());
        dto.setTitle(submission.getTitle());

        return ResponseEntity.ok(dto);
    }


}
