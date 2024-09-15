package management.example.demo.Repository;

import management.example.demo.Model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllBySubmissionId(Long submissionId);

    Feedback save(Feedback feedback);
}
