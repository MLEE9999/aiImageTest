package aitest.domain;

import aitest.InspectionApplication;
import aitest.domain.InspectedImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Result_table")
@Data
//<<< DDD / Aggregate Root
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resultId;

    private String imageId;

    private String status;

    public static ResultRepository repository() {
        ResultRepository resultRepository = InspectionApplication.applicationContext.getBean(
            ResultRepository.class
        );
        return resultRepository;
    }

    //<<< Clean Arch / Port Method
    public static void imageInspection(CreatedImage createdImage) {
        repository().findById(createdImage.getImageId()).ifPresent(result -> {
        // 상태 변경: createdImage에서 필요한 값 가져와서 setStatus 등 호출
        result.setStatus(createdImage.getStatus());

        // 저장
        repository().save(result);

        // 도메인 이벤트 생성 및 발행
        InspectedImage inspectedImage = new InspectedImage(result);
        inspectedImage.publishAfterCommit();
        });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
