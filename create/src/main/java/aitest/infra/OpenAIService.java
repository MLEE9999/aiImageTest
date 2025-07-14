package aitest.infra;

import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.ContentType;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_IMAGE_VARIATION_URL = "https://api.openai.com/v1/images/variations";
    private static final String OPENAI_IMAGE_GENERATION_URL = "https://api.openai.com/v1/images/generations";

    /**
     * 이미지 변형 (Variations)
     */
    public List<String> createImageVariation(MultipartFile imageFile) throws IOException {
        List<String> variationUrls = new ArrayList<>();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(OPENAI_IMAGE_VARIATION_URL);
            post.addHeader("Authorization", "Bearer " + openAiApiKey);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("image", imageFile.getInputStream(),
                    ContentType.create("image/png"), imageFile.getOriginalFilename());
            builder.addTextBody("n", "1");               // 생성 이미지 수
            builder.addTextBody("size", "1024x1024");    // 생성 이미지 크기

            HttpEntity multipart = builder.build();
            post.setEntity(multipart);

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    throw new RuntimeException("OpenAI API 호출 실패, 상태코드: " + statusCode + ", 응답: " + responseBody);
                }

                String json = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);
                JsonNode dataNode = rootNode.get("data");

                for (JsonNode node : dataNode) {
                    String url = node.get("url").asText();
                    variationUrls.add(url);
                }
            }
        }

        return variationUrls;
    }

    /**
     * 텍스트 프롬프트로 이미지 생성 (Generations)
     * @param prompt 텍스트 프롬프트
     * @param model  사용할 DALL·E 모델 (예: "dall-e-3")
     * @return 생성된 이미지 URL 리스트
     * @throws IOException
     */
    public List<String> generateImage(String prompt, String model) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(OPENAI_IMAGE_GENERATION_URL);
            post.addHeader("Authorization", "Bearer " + openAiApiKey);

            // JSON 바디를 Map으로 만든 후 ObjectMapper로 직렬화
            String jsonBody = mapper.writeValueAsString(Map.of(
                "model", model,
                "prompt", prompt,
                "n", 1,
                "size", "1024x1024"
            ));

            post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode != 200) {
                    System.err.println("OpenAI API 호출 실패: 상태코드=" + statusCode + ", 응답: " + responseBody);
                    throw new RuntimeException("OpenAI API 호출 실패: 상태코드=" + statusCode + ", 응답: " + responseBody);
                }

                JsonNode rootNode = mapper.readTree(responseBody);
                JsonNode dataNode = rootNode.get("data");

                for (JsonNode node : dataNode) {
                    imageUrls.add(node.get("url").asText());
                }
            }
        }

        return imageUrls;
    }
}
