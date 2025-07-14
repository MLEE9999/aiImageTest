package aitest.domain;

import aitest.CreateApplication;
import aitest.domain.CreatedImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Ai_table")
@Data
//<<< DDD / Aggregate Root
public class Ai {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imageId;

    private String name;

    private String url;

    private String status;

    @PostPersist
    public void onPostPersist() {
        CreatedImage createdImage = new CreatedImage(this);
        createdImage.publishAfterCommit();
    }

    public static AiRepository repository() {
        AiRepository aiRepository = CreateApplication.applicationContext.getBean(
            AiRepository.class
        );
        return aiRepository;
    }

    //<<< Clean Arch / Port Method
    public static void statusUpdate(InspectedImage inspectedImage) {
        //implement business logic here:

        /** Example 1:  new item 
        Ai ai = new Ai();
        repository().save(ai);

        */

        /** Example 2:  finding and process
        

        repository().findById(inspectedImage.get???()).ifPresent(ai->{
            
            ai // do something
            repository().save(ai);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
