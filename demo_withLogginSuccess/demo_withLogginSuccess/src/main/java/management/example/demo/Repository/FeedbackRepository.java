package management.example.demo.Repository;

import management.example.demo.Model.Feedback;
import management.example.demo.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback , Long> {
    List<Feedback> findById(Submission submission);
}
