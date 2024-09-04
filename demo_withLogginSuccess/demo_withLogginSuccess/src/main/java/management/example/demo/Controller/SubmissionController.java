package management.example.demo.Controller;

import management.example.demo.DTO.FileMetadataDto;
import management.example.demo.DTO.SubmissionDetailDto;
import management.example.demo.Model.Submission;
import management.example.demo.Service.FileService;
import management.example.demo.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private FileService fileService;

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
        dto.setSubmissionStatus(submission.getSubmissionStatus());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("uploaded/{submissionId}")
    public List<FileMetadataDto> getFileMetadataBySubmissionId(@PathVariable Long submissionId) {
        System.out.println("OIERJGIEJGIJF");
        List<FileMetadataDto> fileMetadataDtos = fileService.getFileMetadataBySubmissionId(submissionId);
        for (FileMetadataDto fileMetadataDto: fileMetadataDtos){
            System.out.println(fileMetadataDto);
        }
        return fileMetadataDtos;
    }


}
