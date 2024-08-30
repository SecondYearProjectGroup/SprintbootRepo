package management.example.demo.Controller;

import management.example.demo.Service.FileService;
import management.example.demo.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileUploadService;

    @Autowired
    private StudentService studentService;



//    @GetMapping("/upload")
//    public String showUploadForm(Model model) {
//        return "uploadForm";
//    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        //String message = fileUploadService.uploadFile(file);
        //model.addAttribute("message", message);
        return "uploadForm";
    }

//    @GetMapping("/download/{id}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
//        Resource resource = fileUploadService.downloadFile(id);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFileWithName(@PathVariable String fileName) {
        Resource resource = fileUploadService.downloadFileByName(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


//    @GetMapping("/download/{id}/{attachmentFile}")
//    public ResponseEntity<Resource> downloadFileStudentEnroll(@PathVariable Long id, @PathVariable String attachmentFile) {
//        // Fetch the student using the ID
//        Optional<Student> student = studentService.getStudent(id);
//
//        if (student.isEmpty()) {
//            return ResponseEntity.notFound().build(); // Return 404 if student is not found
//        }
//
//        // Fetch the list of file metadata associated with the student
//        List<FileMetadata> fileMetadatas = studentService.getFileMetadataByStuId(student.get().getId());
//
//        // Iterate through the file metadata and check if any matches the requested file name
//        for (FileMetadata fileMetadata : fileMetadatas) {
//            System.out.println(fileMetadata.getFileName());
//            if ((fileMetadata.getFileName()).equals(attachmentFile)) {
//                // Download the file if the file name matches
//                Resource resource = fileUploadService.downloadFile(fileMetadata.getId());
//                return ResponseEntity.ok()
//                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            }
//        }
//
//        // Return 404 if the file is not found
//        return ResponseEntity.notFound().build();
//    }


}
