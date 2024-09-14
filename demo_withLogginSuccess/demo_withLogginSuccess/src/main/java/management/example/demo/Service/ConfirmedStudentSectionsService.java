package management.example.demo.Service;

import management.example.demo.Model.ConfirmedStudentSections;
import management.example.demo.Model.Tile;
import management.example.demo.Repository.ConfirmedStudentSectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfirmedStudentSectionsService {

    @Autowired
    private ConfirmedStudentSectionsRepository confirmedStudentSectionsRepository;

    public List<ConfirmedStudentSections> getSectionsByRegNumber(String regNumber) {
        return confirmedStudentSectionsRepository.findByregNumber(regNumber);
    }

    public List<ConfirmedStudentSections> getSectionsByRegNumberAndTab(String regNumber, String tab) {
        return confirmedStudentSectionsRepository.findByRegNumberAndActiveTab(regNumber, tab);
    }

    public List<ConfirmedStudentSections> getSectionsByRegNumberAndTabForExaminers(String regNumber, String tab, String tileType) {
        List<ConfirmedStudentSections> sections = confirmedStudentSectionsRepository.findByRegNumberAndActiveTab(regNumber, tab);

        // Filter tiles inside each section based on the tile type
        for (ConfirmedStudentSections section : sections) {
            List<Tile> filteredTiles = section.getTiles().stream()
                    .filter(tile -> tile.getType().equals(tileType))  // Filter tiles by type
                    .collect(Collectors.toList());

            section.setTiles(filteredTiles);  // Update the section with the filtered tiles
        }

        return sections;
    }


    public ConfirmedStudentSections saveSection(ConfirmedStudentSections section) {
        return confirmedStudentSectionsRepository.save(section);
    }

    public void deleteSection(Long id) {
        confirmedStudentSectionsRepository.deleteById(id);
    }
}
