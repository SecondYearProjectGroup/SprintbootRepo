package management.example.demo.Repository;

import management.example.demo.Model.Supervisor;
import management.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
    Optional<Supervisor> findById(Long id);
    User save(User user);

}