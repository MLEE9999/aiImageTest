package aitest.domain;

import aitest.domain.*;
import aitest.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class InspectedImage extends AbstractEvent {

    private Long id;

    public InspectedImage(Result aggregate) {
        super(aggregate);
    }

    public InspectedImage() {
        super();
    }
}
//>>> DDD / Domain Event
