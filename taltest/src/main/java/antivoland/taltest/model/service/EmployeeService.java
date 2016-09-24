package antivoland.taltest.model.service;

import antivoland.taltest.model.Employee;
import antivoland.taltest.model.dao.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeDAO employeeDAO;

    public List<Employee> list() {
        return employeeDAO.list();
    }

    public void add(Employee employee) throws EmployeeAlreadyExistsException {
        try {
            employeeDAO.insert(employee);
        } catch (DuplicateKeyException e) {
            throw new EmployeeAlreadyExistsException(employee.id);
        }
    }

    public void edit(Employee employee) throws EmployeeNotFoundException {
        int updated = employeeDAO.update(employee);
        if (updated == 0) {
            throw new EmployeeNotFoundException(employee.id);
        }
    }

    public void remove(String id) throws EmployeeNotFoundException {
        int deleted = employeeDAO.delete(id);
        if (deleted == 0) {
            throw new EmployeeNotFoundException(id);
        }
    }
}
