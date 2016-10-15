package antivoland.taltest.model.service;

import antivoland.taltest.model.Relation;
import antivoland.taltest.model.dao.RelationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationService {
    @Autowired
    RelationDAO relationDAO;

    public List<Relation> list() {
        return relationDAO.list();
    }

    public void add(Relation relation) throws RelationAlreadyExistsException {
        try {
            relationDAO.insert(relation);
        } catch (DuplicateKeyException e) {
            throw new RelationAlreadyExistsException(relation.managerId, relation.employeeId);
        }
    }

    public void remove(String managerId, String employeeId) throws RelationNotFoundException {
        int deleted = relationDAO.delete(managerId, employeeId);
        if (deleted == 0) {
            throw new RelationNotFoundException(managerId, employeeId);
        }
    }

    public void removeAll(String employeeId) {
        relationDAO.deleteAll(employeeId);
    }
}
