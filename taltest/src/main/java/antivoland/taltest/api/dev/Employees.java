package antivoland.taltest.api.dev;

import antivoland.taltest.api.dev.domain.EmployeeDetails;
import antivoland.taltest.api.dev.domain.Failure;
import antivoland.taltest.model.Employee;
import antivoland.taltest.model.service.EmployeeAlreadyExistsException;
import antivoland.taltest.model.service.EmployeeNotFoundException;
import antivoland.taltest.model.service.EmployeeService;
import antivoland.taltest.model.service.RelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dev/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class Employees {
    private static final Logger LOG = LoggerFactory.getLogger(Employees.class);
    private static final String LIST = "Listing employees";
    private static final String ADD = "Adding employee '%s'";
    private static final String EDIT = "Editing employee '%s'";
    private static final String REMOVE = "Removing employee '%s'";

    @Autowired
    EmployeeService employeeService;
    @Autowired
    RelationService relationService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> list() {
        LOG.debug(LIST);
        return employeeService.list();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public void add(@PathVariable("id") String id, @RequestBody EmployeeDetails details) throws EmployeeAlreadyExistsException {
        LOG.debug(String.format(ADD, id));
        employeeService.add(new Employee(id, details));
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void edit(@PathVariable("id") String id, @RequestBody EmployeeDetails details) throws EmployeeNotFoundException {
        LOG.debug(String.format(EDIT, id));
        employeeService.edit(new Employee(id, details));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public void remove(@PathVariable("id") String id) throws EmployeeNotFoundException {
        LOG.debug(String.format(REMOVE, id));
        relationService.removeAll(id);
        employeeService.remove(id);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Failure employeeNotFound(EmployeeNotFoundException e) {
        return new Failure(e);
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Failure employeeAlreadyExists(EmployeeAlreadyExistsException e) {
        return new Failure(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Failure failure(Exception e) {
        return new Failure(e);
    }
}
