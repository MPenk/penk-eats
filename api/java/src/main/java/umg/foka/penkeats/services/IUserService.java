package umg.foka.penkeats.services;

import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;

public interface IUserService {

    User validateUser(String name, String password) throws AuthException;
    User registerUser(String name, String password, String defaultaddress) throws AuthException;
}
