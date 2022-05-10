package umg.foka.penkeats.repositories;

import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;

public interface IUserRepository {

    Integer create(String name, String password, String defaultaddress) throws AuthException;

    User findByNameAndPassword(String name, String password) throws AuthException;

    Integer getCountByName(String name);

    User findById(Integer userid);

}
