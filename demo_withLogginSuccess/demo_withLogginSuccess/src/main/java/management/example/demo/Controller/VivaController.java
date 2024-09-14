package management.example.demo.Controller;

import management.example.demo.DTO.VIvaDetailsDto;
import management.example.demo.Model.Viva;
import management.example.demo.Service.VivaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/viva")
public class VivaController {

    @Autowired
    private VivaService vivaService;

    @GetMapping("/getVivaDetails/{tileId}")
    public ResponseEntity<VIvaDetailsDto> getVivaDetails(@PathVariable(name = "tileId") Long tileId) {
        Viva viva = vivaService.get(tileId);
        if (viva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Create a DTO to send only the required data
        VIvaDetailsDto dto = new VIvaDetailsDto();
        dto.setVivaDate(viva.getVivaDate());
        dto.setTitle(viva.getTitle());
        dto.setComments(viva.getComments());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/submitComments/{vivaId}")
    public ResponseEntity<String> submitComments(@PathVariable Long vivaId, @RequestBody Map<String, String> request) {
        Viva viva = vivaService.get(vivaId);
        String comments = request.get("comments");
        viva.setComments(comments);
        vivaService.saveViva(viva);
        return ResponseEntity.ok("Comments submitted successfully.");
    }
}