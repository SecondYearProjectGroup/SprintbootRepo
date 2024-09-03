package management.example.demo.Repository;

import management.example.demo.Model.Forum;
import management.example.demo.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Long> {
    List<Forum> findById(Submission submission);

    Forum save(Forum forum);
}
