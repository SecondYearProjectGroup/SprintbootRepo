package management.example.demo.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import management.example.demo.Model.EmailTemplate;
import management.example.demo.Repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private TemplateEngine stringTemplateEngine;

    // Simple Email
    public void sendMail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("dasunikawya2001@gmail.com");
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
        helper.setFrom("dasunikawya2001@gmail.com");

        // Path to the file
        String pathToAttachment = "C:\\Users\\ASUS\\Desktop\\Com Sem 4\\CO200 Second Year Project\\Required Docs\\RPGapplication.pdf";
        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));

        // Add the attachment
        helper.addAttachment("RPGapplication.pdf", file, "application/pdf");

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





    //To load all the templates to admin
    public List<EmailTemplate> getAllTemplates() {
        return emailTemplateRepository.findAll();
    }

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
