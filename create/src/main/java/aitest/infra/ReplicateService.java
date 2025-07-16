package aitest.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReplicateService {

    @Value("${replicate.api.key}")
    private String API_TOKEN;
    private static final String API_URL = "https://api.replicate.com/v1/predictions";

    public String generateStyledImage(String imageUrl, String prompt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> input = new HashMap<>();
        input.put("prompt", prompt);
        input.put("image", imageUrl);

        Map<String, Object> payload = new HashMap<>();
        payload.put("version", "c8e4ec18eb84bb89f8d9aab73e4740be0b0aa3476e9f8b9473f11e3f4dbf08be"); // SDXL 모델 버전
        payload.put("input", input);

        String json = mapper.writeValueAsString(payload);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Authorization", "Token " + API_TOKEN);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            if (statusCode >= 200 && statusCode < 300) {
                return responseBody;
            } else {
                throw new IOException("Replicate API 호출 실패, 상태 코드: " + statusCode + ", 내용: " + responseBody);
            }
        }
    }
}
