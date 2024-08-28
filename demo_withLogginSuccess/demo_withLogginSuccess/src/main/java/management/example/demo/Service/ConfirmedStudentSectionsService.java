package management.example.demo.Service;

import management.example.demo.Model.ConfirmedStudentSections;
import management.example.demo.Repository.ConfirmedStudentSectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfirmedStudentSectionsService {

    @Autowired
    private ConfirmedStudentSectionsRepository confirmedStudentSectionsRepository;

    public List<ConfirmedStudentSections> getSectionsByRegNumber(String regNumber) {
        return confirmedStudentSectionsRepository.findByregNumber(regNumber);
    }

    public ConfirmedStudentSections saveSection(ConfirmedStudentSections section) {
        return confirmedStudentSectionsRepository.save(section);
    }

    public void deleteSection(Long id) {
        confirmedStudentSectionsRepository.deleteById(id);
    }
}
