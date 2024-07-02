package management.example.demo.Controller;

import ch.qos.logback.core.model.Model;
import management.example.demo.Model.Student;
import management.example.demo.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/enroll")
    public String showEnrollmentForm() {
        return "enroll";  // enroll.html should be your form page
    }

    @PostMapping("/enroll")
    public String enrollStudent(Student student, Model model) {
        studentService.saveStudent(student);
        model.addText("Student enrolled successfully!");
        return "enroll";
    }

}