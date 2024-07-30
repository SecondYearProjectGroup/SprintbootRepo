package management.example.demo.Repository;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmedStudentRepository extends JpaRepository<ConfirmedStudent, Long> {
    User save(User user);
}
