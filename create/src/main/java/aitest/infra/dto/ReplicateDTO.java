package aitest.infra.dto;

import lombok.Data;

// DTO 클래스 (내부 static class 또는 별도 파일로 분리 가능)
@Data
public class ReplicateDTO {
    private String imageUrl;
    private String prompt;
}