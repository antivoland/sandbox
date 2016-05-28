package antivoland.oftest.model.service;

import antivoland.oftest.model.UserPoint;
import antivoland.oftest.model.dao.UserPointDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserPointService {
    @Autowired
    UserPointDAO userPointDAO;

    public UserPoint get(int userId) throws UserPointNotFoundException {
        try {
            return userPointDAO.find(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserPointNotFoundException(userId);
        }
    }

    public void create(UserPoint userPoint) throws UserPointAlreadyExistsException {
        try {
            userPointDAO.insert(userPoint);
        } catch (DuplicateKeyException e) {
            throw new UserPointAlreadyExistsException(userPoint.userId);
        }
    }

    public void track(UserPoint userPoint) throws UserPointNotFoundException {
        int updated = userPointDAO.update(userPoint);
        if (updated == 0) {
            throw new UserPointNotFoundException(userPoint.userId);
        }
    }

    public void remove(int userId) {
        userPointDAO.delete(userId);
    }
}
