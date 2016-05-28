package antivoland.oftest.model.service;

import antivoland.oftest.model.UserPoint;
import antivoland.oftest.model.dao.UserPointDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class UserPointService {
    @Autowired
    UserPointDAO userPointDAO;

    public void create(int userId, double lat, double lon) throws UserPointAlreadyExistsException {
        try {
            userPointDAO.insert(new UserPoint(userId, lat, lon));
        } catch (DuplicateKeyException e) {
            throw new UserPointAlreadyExistsException(userId);
        }
    }

    public void track(int userId, double lat, double lon) throws UserPointNotFoundException {
        int updated = userPointDAO.update(new UserPoint(userId, lat, lon));
        if (updated == 0) {
            throw new UserPointNotFoundException(userId);
        }
    }

    public void remove(int userId) {
        userPointDAO.delete(userId);
    }
}
