package management.example.demo.Service;

import management.example.demo.Model.Viva;
import management.example.demo.Repository.VivaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VivaService {

    @Autowired
    private VivaRepository vivaRepository;

    public Viva saveViva(Viva viva) {
        return vivaRepository.save(viva);
    }
}
