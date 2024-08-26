package management.example.demo.Controller;

import management.example.demo.Model.ConfirmedStudentSections;
import management.example.demo.Service.ConfirmedStudentSectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class ConfirmedStudentSectionsController {

    @Autowired
    private ConfirmedStudentSectionsService confirmedStudentSectionsService;

    @GetMapping("/{regNumber}")
    public ResponseEntity<List<ConfirmedStudentSections>> getSectionsByRegNumber(@PathVariable String regNumber) {
        List<ConfirmedStudentSections> sections = confirmedStudentSectionsService.getSectionsByRegNumber(regNumber);
        return ResponseEntity.ok(sections);
    }

    @PostMapping
    public ResponseEntity<ConfirmedStudentSections> saveSection(@RequestBody ConfirmedStudentSections section) {
        System.out.println("Received section: " + section);
        section.getTiles().forEach(tile -> System.out.println("Tile: " + tile));
        ConfirmedStudentSections savedSection = confirmedStudentSectionsService.saveSection(section);
        return ResponseEntity.ok(savedSection);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        confirmedStudentSectionsService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}