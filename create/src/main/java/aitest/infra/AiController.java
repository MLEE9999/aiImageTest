package aitest.infra;

import aitest.infra.OpenAIService;
import aitest.domain.*;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value="/aicreate")
@Transactional
public class AiController {

    @Autowired
    AiRepository aiRepository;

    @Autowired
    private OpenAIService openAIService;

    // 이미지 변형 API (variation)
    @PostMapping("/variations")
    public ResponseEntity<?> createVariation(@RequestParam("image") MultipartFile image) {
        try {
            List<String> variationUrls = openAIService.createImageVariation(image);
            return ResponseEntity.ok(variationUrls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to create image variation: " + e.getMessage());
        }
    }

    // 텍스트 프롬프트로 이미지 생성 API (generation)
    @PostMapping("/generate")
    public ResponseEntity<?> generateImage(@RequestParam("prompt") String prompt,
                                           @RequestParam(value = "model", required = false, defaultValue = "dall-e-3") String model) {
        try {
            List<String> generatedUrls = openAIService.generateImage(prompt, model);
            return ResponseEntity.ok(generatedUrls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to generate image: " + e.getMessage());
        }
    }
}
