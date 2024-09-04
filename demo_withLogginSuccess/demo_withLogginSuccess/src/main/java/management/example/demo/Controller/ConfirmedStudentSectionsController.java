package management.example.demo.Controller;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.ConfirmedStudentSections;
import management.example.demo.Model.Forum;
import management.example.demo.Model.Submission;
import management.example.demo.Service.ConfirmedStudentSectionsService;
import management.example.demo.Service.ConfirmedStudentService;
import management.example.demo.Service.ForumService;
import management.example.demo.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class ConfirmedStudentSectionsController {

    @Autowired
    private ConfirmedStudentSectionsService confirmedStudentSectionsService;

    @Autowired
    private ConfirmedStudentService confirmedStudentService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ForumService forumService;

    @GetMapping("/{regNumber}")
    public ResponseEntity<List<ConfirmedStudentSections>> getSectionsByRegNumber(@PathVariable String regNumber) {
        List<ConfirmedStudentSections> sections = confirmedStudentSectionsService.getSectionsByRegNumber(regNumber);
        return ResponseEntity.ok(sections);
    }

    @PostMapping
    public ResponseEntity<ConfirmedStudentSections> saveSection(@RequestBody ConfirmedStudentSections section) {
        System.out.println("Received section: " + section);
        // Retrieve the student associated with the section
        String sectionStudent = section.getRegNumber();

        // Set the student attribute for each tile to match the section's student
        section.getTiles().forEach(tile -> {
            tile.setRegNumber(sectionStudent);
            System.out.println("Updated Tile with Student: " + tile);
            ConfirmedStudent confirmedStudent = confirmedStudentService.get(sectionStudent);

            //Save in the submission repository
            if(tile.getType().equals("submission")){
                Submission submission = new Submission();
                submission.setTile(tile);
                submission.setConfirmedStudent(confirmedStudent);
                submission.setTitle(tile.getTitle());
                submission.setSubmissionStatus(false);
                submissionService.saveSubmissionsParameters(submission);
            } else if (tile.getType().equals("forum")) {
                Forum forum = new Forum();
                forum.setConfirmedStudent(confirmedStudent);
                forumService.saveForum(forum);
            }
        });


        // Save the section with the tiles now correctly linked to the student
        ConfirmedStudentSections savedSection = confirmedStudentSectionsService.saveSection(section);
        return ResponseEntity.ok(savedSection);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        confirmedStudentSectionsService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}