package aitest.domain;

import aitest.domain.*;
import aitest.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class CreatedImage extends AbstractEvent {

    private Long imageId;
    private String name;
    private String url;
    private String Status;
}
