package antivoland.taltest.api.dev;

import antivoland.taltest.api.dev.domain.Failure;
import antivoland.taltest.model.Relation;
import antivoland.taltest.model.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping(value = "/api/dev/managers", produces = MediaType.APPLICATION_JSON_VALUE)
public class Managers {
    private static final Logger LOG = LoggerFactory.getLogger(Managers.class);
    private static final String LIST = "Listing managers";
    private static final String ATTACH = "Adding relation between manager '%s' and employee '%s'";
    private static final String DETACH = "Removing relation between manager '%s' and employee '%s'";

    @Autowired
    RelationService relationService;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, List<String>> list() {
        LOG.debug(LIST);
        List<Relation> relations = relationService.list();
        return relations.stream().collect(groupingBy(r -> r.managerId, mapping(r -> r.employeeId, toList())));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}/employees/{employeeId}")
    public void attach(@PathVariable("id") String id, @PathVariable("employeeId") String employeeId) throws RelationAlreadyExistsException {
        LOG.debug(String.format(ATTACH, id, employeeId));
        relationService.add(new Relation(id, employeeId));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}/employees/{employeeId}")
    public void detach(@PathVariable("id") String id, @PathVariable("employeeId") String employeeId) throws RelationNotFoundException {
        LOG.debug(String.format(DETACH, id, employeeId));
        relationService.remove(id, employeeId);
    }

    @ExceptionHandler(RelationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Failure relationNotFound(RelationNotFoundException e) {
        return new Failure(e);
    }

    @ExceptionHandler(RelationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Failure relationAlreadyExists(RelationAlreadyExistsException e) {
        return new Failure(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Failure failure(Exception e) {
        return new Failure(e);
    }
}
