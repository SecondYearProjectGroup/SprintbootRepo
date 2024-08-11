package management.example.demo.Service;

import management.example.demo.Model.Notification;
import management.example.demo.Model.User;
import management.example.demo.Repository.NotificationRepository;
import management.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    //To get all the notifications
    public List<Notification> getAllNotifications() {
        // Get the current authentication object - USER
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract the username from the authentication object
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return notificationRepository.findByUser(user);
    }

    //Create a notification
    public  Notification createNotification(String username,String title, String message){
        User user = userRepository.findByUsername(username);
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setUser(user);
        return notificationRepository.save(notification);
    }
}
