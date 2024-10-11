package management.example.demo.Controller;

import management.example.demo.Model.EmailTemplate;
import management.example.demo.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    //Send the email
    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String toEmail, @RequestParam String subject, @RequestParam String body) {
        emailService.sendMail(toEmail, subject, body);
        return "Email sent successfully!";
    }

    @GetMapping
    public List<EmailTemplate> getAllTemplates() {
        return emailService.getAllTemplates();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public EmailTemplate updateTemplate(@PathVariable Long id, @RequestBody EmailTemplate templateDetails) throws Exception {
        return emailService.updateTemplate(id, templateDetails);
    }

}
