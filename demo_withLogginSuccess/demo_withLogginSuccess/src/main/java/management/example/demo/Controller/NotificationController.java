package management.example.demo.Controller;

import management.example.demo.Model.Notification;
import management.example.demo.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

//    @GetMapping("/notifications")
//    public List<Notification> getAllNotifications(@RequestParam String username){
//        return  notificationService.getAllNotifications();
//    }

    @PostMapping("/notifications")
    public Notification createNotification(@RequestParam String username,@RequestParam String title, @RequestParam String message){
        return notificationService.createNotification(username,title, message);
    }
}
