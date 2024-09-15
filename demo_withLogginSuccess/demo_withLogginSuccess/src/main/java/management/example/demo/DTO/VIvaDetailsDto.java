package management.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VIvaDetailsDto {


    private String title;
    private LocalDateTime vivaDate;
    private String comments;

}
