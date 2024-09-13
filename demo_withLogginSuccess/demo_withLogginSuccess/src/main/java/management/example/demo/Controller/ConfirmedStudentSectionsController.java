package management.example.demo.Controller;

import management.example.demo.Model.*;
import management.example.demo.Service.*;
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

    @Autowired
    private VivaService vivaService;

//    @GetMapping("/{regNumber}/{tab}")
//    public ResponseEntity<List<ConfirmedStudentSections>> getSectionsByRegNumberAndTab(@PathVariable String regNumber, @PathVariable String tab) {
//        List<ConfirmedStudentSections> sections = confirmedStudentSectionsService.getSectionsByRegNumber(regNumber);
//
//        return ResponseEntity.ok(sections);
//    }

    @GetMapping("/{regNumber}/{tab}")
    public ResponseEntity<List<ConfirmedStudentSections>> getSectionsByRegNumberAndTab(@PathVariable String regNumber, @PathVariable String tab) {
        List<ConfirmedStudentSections> sections = confirmedStudentSectionsService.getSectionsByRegNumberAndTab(regNumber, tab);
        return ResponseEntity.ok(sections);
    }

    @PostMapping
    public ResponseEntity<ConfirmedStudentSections> saveSection(@RequestBody ConfirmedStudentSections section) {
        System.out.println("Received section: " + section);
        // Retrieve the student associated with the section
        String sectionStudent = section.getRegNumber();

        //Set the tab
        System.out.println(section.getActiveTab());

        // Set the student attribute for each tile to match the section's student
        section.getTiles().forEach(tile -> {
            tile.setRegNumber(sectionStudent);
            System.out.println("Updated Tile with Student: " + tile);
            ConfirmedStudent confirmedStudent = confirmedStudentService.get(sectionStudent);

            //Save in the submission repository
            if(tile.getType().equals("submission")){

                //For pre-submission (for supervisor feedbacks)
                Submission preSubmission = new Submission();
                preSubmission.setTile(tile);
                preSubmission.setConfirmedStudent(confirmedStudent);
                preSubmission.setTitle("Pre Submission" + tile.getTitle());
                preSubmission.setSubmissionStatus(false);
                submissionService.saveSubmissionsParameters(preSubmission);
//
//                //For final submission (for examiner reviews)
//                Submission finalSubmission = new Submission();
//                finalSubmission.setTile(tile);
//                finalSubmission.setConfirmedStudent(confirmedStudent);
//                finalSubmission.setTitle(tile.getTitle());
//                finalSubmission.setSubmissionStatus(false);
//                submissionService.saveSubmissionsParameters(finalSubmission);
            } else if (tile.getType().equals("forum")) {
                Forum forum = new Forum();
                forum.setTile(tile);
                forum.setConfirmedStudent(confirmedStudent);
                forumService.saveForum(forum);
            }
            else if (tile.getType().equals("viva")){
                Viva viva = new Viva();
                viva.setTile(tile);
                viva.setConfirmedStudent(confirmedStudent);
                vivaService.saveViva(viva);
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