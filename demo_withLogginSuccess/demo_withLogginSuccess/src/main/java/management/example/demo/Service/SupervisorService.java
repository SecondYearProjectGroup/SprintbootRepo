package management.example.demo.Service;

import management.example.demo.Model.Supervisor;
import management.example.demo.Repository.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorService {
    @Autowired
    private SupervisorRepository supervisorRepository;

    public List<Supervisor> listAll() {
        return supervisorRepository.findAll();
    }
}
