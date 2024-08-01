package management.example.demo.Controller;

import management.example.demo.Service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping()
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        String message = fileUploadService.uploadFile(file);
        model.addAttribute("message", message);
        return "uploadForm";
    }
}
