package management.example.demo.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file);
}
