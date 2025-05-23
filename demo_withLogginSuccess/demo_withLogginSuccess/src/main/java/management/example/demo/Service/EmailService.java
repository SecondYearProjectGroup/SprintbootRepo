package management.example.demo.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import management.example.demo.Model.EmailTemplate;
import management.example.demo.Model.User;
import management.example.demo.Repository.EmailTemplateRepository;
import management.example.demo.Repository.UserRepository;
import management.example.demo.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.*;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private TemplateEngine stringTemplateEngine;
    @Autowired
    private UserRepository userRepository;

    // Simple Email
    public void sendMail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("thathsaraishan@gmail.com");
        mailSender.send(message);
    }

    // Email with HTML content (links, images, and more)
    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("myemail@gmail.com");

        mailSender.send(mimeMessage);
    }

    // Email with an attachment
    public void sendEmailWithAttachment(String toEmail, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);
        //helper.setFrom("myemail@gmail.com");
        //helper.addAttachment(file.getName(), file);
        //Path to the file
        String pathToAttachment = "D:\\Other values\\CSS+cheat+sheet.pdf";

        //Define the file system resource
        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        //Attachment
        helper.addAttachment("CSS+cheat+sheet.pdf", file , "application/pdf");

        // Send the email
        mailSender.send(mimeMessage);
    }

    public void sendEmail(String templateName, Map<String, Object> variables, String toEmail) throws MessagingException {
        // Fetch the template from the repository
        EmailTemplate template = emailTemplateRepository.findByName(templateName)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        // Prepare the Thymeleaf context with the variables
        Context context = new Context();
        context.setVariables(variables);

        // Replace new lines in the body with <br> tags for HTML formatting
        String bodyWithLineBreaks = template.getBody().replace("\n", "<br>");

        // Wrap the modified body content in a Thymeleaf inline block
        String wrappedTemplateContent = String.format("<div th:inline=\"text\">%s</div>", bodyWithLineBreaks);

        // Process the wrapped content using Thymeleaf
        String processedBody = stringTemplateEngine.process(wrappedTemplateContent, context);

        // Create and send the email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject(template.getSubject());
        helper.setText(processedBody, true); // true to send as HTML

        mailSender.send(message);
    }






//    public List<EmailTemplate> getAllTemplates() {
//        return emailTemplateRepository.findAll();
//    }

    //To load all the templates to admin
    public List<EmailTemplate> getAllTemplatesForAdminAndDefaults() {
        Role adminRole = Role.ADMIN; // Replace with your actual method of getting Role.ADMIN
        Optional<User> adminUserOptional = userRepository.findByRolesContaining(adminRole);

        if (adminUserOptional.isPresent()) {
            Long adminUserId = adminUserOptional.get().getId();
            // Assuming "default" is the type indicating a default template
            String defaultType = "default";
            return emailTemplateRepository.findByUserIdOrType(adminUserId, defaultType);
        }

        return Collections.emptyList(); // or handle it differently if no admin user is found
    }

//    public List<EmailTemplate> getAllTemplatesForAdmin() {
//        // Assuming Role.ADMIN is an enum and you have a way to get the Role entity
//        Role adminRole = Role.ADMIN; // Replace with your actual way of getting Role.ADMIN
//        Optional<User> adminUserOptional = userRepository.findByRolesContaining(adminRole);
//
//        if (adminUserOptional.isPresent()) {
//            Long adminUserId = adminUserOptional.get().getId();
//            return emailTemplateRepository.findByUserId(adminUserId);
//        }
//
//        return Collections.emptyList(); // or throw an exception if no admin user is found
//    }

    public EmailTemplate getTemplateById(Long id) throws Exception {
        return emailTemplateRepository.findById(id).orElseThrow(() -> new Exception("Template not found"));
    }

    public EmailTemplate updateTemplate(Long id, EmailTemplate templateDetails) throws Exception {
        EmailTemplate template = getTemplateById(id);
        template.setName(templateDetails.getName());
        template.setSubject(templateDetails.getSubject());
        template.setBody(templateDetails.getBody());
        return emailTemplateRepository.save(template);
    }

    public EmailTemplate addNewTemplate(EmailTemplate emailTemplate){
        return emailTemplateRepository.save(emailTemplate);
    }

    //Send mail with the template and provided email address
    public void sendEmailWithGivenDetailsToStudents(EmailTemplate emailTemplate, List<String> emails) throws MessagingException {
        for (String email: emails){
            Map<String, Object> variables = new HashMap<>();
            sendEmail(emailTemplate.getName(), variables, email.toString());
        }
    }

    public void sendEmailWithGivenDetails(Map<String, Object> payload) throws MessagingException {
        EmailTemplate emailTemplate = new ObjectMapper().convertValue(payload.get("template"), EmailTemplate.class);
        List<String> emails = new ObjectMapper().convertValue(payload.get("emails"), new TypeReference<List<String>>(){});

        for (String email : emails) {
            Map<String, Object> variables = new HashMap<>();
            sendEmail(emailTemplate.getName(), variables, email);
            System.out.println("Sending email to: " + email);  // Debugging log
        }
    }


}












/*

common MIME types and their corresponding file extensions:

Text Files:
.txt: text/plain
.html: text/html
.css: text/css
.csv: text/csv

Image Files:
.jpg, .jpeg: image/jpeg
.png: image/png
.gif: image/gif
.bmp: image/bmp

Application Files:
.pdf: application/pdf
.doc: application/msword
.docx: application/vnd.openxmlformats-officedocument.wordprocessingml.document
.xls: application/vnd.ms-excel
.xlsx: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
.zip: application/zip

Audio and Video Files:
.mp3: audio/mpeg
.wav: audio/wav
.mp4: video/mp4
.avi: video/x-msvideo'

 */
