package aitest.domain;

import aitest.domain.*;
import aitest.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CreatedImage extends AbstractEvent {

    private Long imageId;
    private String name;
    private String url;

    public CreatedImage(Ai aggregate) {
        super(aggregate);
    }

    public CreatedImage() {
        super();
    }
}
//>>> DDD / Domain Event
