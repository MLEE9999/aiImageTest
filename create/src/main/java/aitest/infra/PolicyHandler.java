package aitest.infra;

import aitest.config.kafka.KafkaProcessor;
import aitest.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    AiRepository aiRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='InspectedImage'"
    )
    public void wheneverInspectedImage_StatusUpdate(
        @Payload InspectedImage inspectedImage
    ) {
        InspectedImage event = inspectedImage;
        System.out.println(
            "\n\n##### listener StatusUpdate : " + inspectedImage + "\n\n"
        );

        // Sample Logic //
        Ai.statusUpdate(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
