package management.example.demo.Repository;

import management.example.demo.Model.ConfirmedStudent;
import management.example.demo.Model.Submission;
import management.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Optional<Submission> findById(Long id);

    List<Submission> findByConfirmedStudent(ConfirmedStudent confirmedStudent);

    User save(User user);
}
