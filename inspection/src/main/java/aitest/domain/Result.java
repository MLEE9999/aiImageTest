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
        //implement business logic here:

        /** Example 1:  new item 
        Result result = new Result();
        repository().save(result);

        InspectedImage inspectedImage = new InspectedImage(result);
        inspectedImage.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        // if createdImage.dalleId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<, Object> aiMap = mapper.convertValue(createdImage.getDalleId(), Map.class);

        repository().findById(createdImage.get???()).ifPresent(result->{
            
            result // do something
            repository().save(result);

            InspectedImage inspectedImage = new InspectedImage(result);
            inspectedImage.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
