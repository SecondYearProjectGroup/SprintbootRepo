package management.example.demo.Repository;

import management.example.demo.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndReadFalse(Long userId);

    //Method to get the count of unread notifications
    long countByUserIdAndReadFalse(Long userId);
}
