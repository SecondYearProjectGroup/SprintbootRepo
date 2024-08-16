package management.example.demo.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    //Simple Email
    public void sendMail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("sameekumarasinghe@gmail.com");
        mailSender.send(message);
    }


    //Email with HTML content ( links, images, and more )
    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("myemail@gmail.com");

        mailSender.send(mimeMessage);
    }


    //Email with an attachment
    //With this setup , send multiple emails by iterating over the list of recipients can be handled.
    public void sendEmailWithAttachment(String toEmail, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //Create the helper class object to simplify the action in the complex mail sending
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);
        //helper.setFrom("myemail@gmail.com");
        //helper.addAttachment(file.getName(), file);
        //Path to the file
        String pathToAttachment = "D:\\Semester 4\\CO200 Computer Engineering Project\\Required Documents\\PG_application.pdf";

        //Define the file system resource
        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        //Attachment
        helper.addAttachment("PG_application.pdf", file , "application/pdf");

        //Send the email
        mailSender.send(mimeMessage);
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
