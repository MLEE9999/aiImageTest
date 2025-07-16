package aitest.infra;

import aitest.domain.AiRepository;
import aitest.infra.dto.ReplicateDTO;
import aitest.infra.dto.AzureUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(value = "/aicreate")
@Transactional
public class AiController {

    @Autowired
    AiRepository aiRepository;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private ReplicateService replicateService;

    @Autowired
    private AzureBlobService azureBlobService;

    // 1. OpenAI 이미지 변형 (Variation)
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

    // 2. OpenAI 텍스트 기반 이미지 생성 (Generation)
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

    // 3. Replicate 스타일 리터칭 (이미지+프롬프트 기반)
    @PostMapping("/replicate")
    public ResponseEntity<?> stylizeImage(@RequestBody ReplicateDTO request) {
        // 💡 요청 값 로그 출력
        System.out.println("====[ /replicate 요청 도착 ]====");
        System.out.println("imageUrl = " + request.getImageUrl());
        System.out.println("prompt = " + request.getPrompt());
        if (request.getImageUrl() == null || request.getPrompt() == null) {
            return ResponseEntity.badRequest().body("imageUrl and prompt are required");
        }

        try {
            String responseJson = replicateService.generateStyledImage(request.getImageUrl(), request.getPrompt());
            return ResponseEntity.ok(responseJson);
        } catch (Exception e) {
            e.printStackTrace();  // 👈 스택 트레이스 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to stylize image: " + e.getMessage());
        }
    }
    // 4. 에저에 이미지 업로드(공개 URL로 변환하기 위해)
    @PostMapping("/upload-to-azure")
    public ResponseEntity<?> uploadToAzure(@RequestBody AzureUploadDTO dto) {
        try {
            String uploadedUrl = azureBlobService.uploadImageFromUrl(dto.getImageUrl());
            return ResponseEntity.ok(uploadedUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Azure 업로드 실패: " + e.getMessage());
        }
    }

    @PostMapping("/upload-to-azure-replicate")
    public ResponseEntity<?> uploadToAzureReplicate(@RequestBody AzureUploadDTO dto) {
        try {
            String uploadedUrl = azureBlobService.uploadImageReplicateUrl(dto.getImageUrl());
            return ResponseEntity.ok(uploadedUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Azure 업로드 실패: " + e.getMessage());
        }
    }
}
