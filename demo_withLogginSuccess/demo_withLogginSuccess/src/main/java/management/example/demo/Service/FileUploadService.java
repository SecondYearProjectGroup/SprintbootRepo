package management.example.demo.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {
    @Value("${upload.path}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(path, bytes);
            String fileName = file.getOriginalFilename();
            String filePath = path.toString();
            return fileName; // Return the file path
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed";
        }
    }
}




//@Service
//public class FileUploadService {
//    @Value("${upload.path}")
//    private String uploadDir;
//
//    public String uploadFile(MultipartFile file) {
//        if (file.isEmpty()) {
//            return "Please select a file to upload";
//        }
//
//        try {
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(uploadDir + file.getOriginalFilename());
//            Files.write(path, bytes);
//            return "You successfully uploaded '" + file.getOriginalFilename() + "'";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "File upload failed";
//        }
//    }
//}
