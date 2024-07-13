package management.example.demo.Controller;

import management.example.demo.Model.Student;
import management.example.demo.Service.EnrolledStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class EnrolledStudentController {

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditStudentPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("new");
        Student student = enrolledStudentService.get(id);
        mav.addObject("student", student);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deletestudent(@PathVariable(name = "id") int id) {
        enrolledStudentService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/Enrolledstu")
    public String viewHomePage(Model model) {
        List<Student> liststudent = enrolledStudentService.listAll();
        model.addAttribute("liststudent", liststudent);
        System.out.print("Get / ");
        return "Enrolledstu";
    }
}
