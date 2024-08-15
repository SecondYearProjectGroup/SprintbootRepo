package management.example.demo.Service;

import management.example.demo.Model.Notification;
import management.example.demo.Model.User;
import management.example.demo.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;



import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendNotification(User user, String title, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);

        // Send notification via WebSocket
        messagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/notifications", notification);
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserIdAndReadFalse(user.getId());
    }

    @Transactional
    public void markAsRead(List<Long> notificationIds) {
        notificationRepository.findAllById(notificationIds).forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
