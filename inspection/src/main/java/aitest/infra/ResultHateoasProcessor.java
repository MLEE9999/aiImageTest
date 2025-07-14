package aitest.infra;

import aitest.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class ResultHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Result>> {

    @Override
    public EntityModel<Result> process(EntityModel<Result> model) {
        return model;
    }
}
