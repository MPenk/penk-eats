package umg.foka.penkeats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;
import umg.foka.penkeats.repositories.IUserRepository;
import umg.foka.penkeats.repositories.UserRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    public boolean promote(int id) {
        var data = userRepository.getUserById(id).get(0);
        var permissions = Integer.parseInt(data.get("permissions").toString());
        if(permissions>=2)
            return false;
        permissions++;
        return userRepository.promote(id, permissions);
    }
    public boolean degrade(int id) {
        var data = userRepository.getUserById(id).get(0);
        var permissions = Integer.parseInt(data.get("permissions").toString());
        if(permissions!=1)
            return false;
        return userRepository.degrade(id);
    }
    public List<Map<String, Object>> getAll() throws AuthException {
        return userRepository.getAll();
    }

    public List<Map<String, Object>> getUserById(int id) throws AuthException {
        return userRepository.getUserById(id);
    }

    public List<Map<String, Object>> getOrdersById(int id) throws AuthException {
        var list = userRepository.getOrdersById(id);
        return repairDate(list);
    }

    static List<Map<String, Object>> repairDate(List<Map<String, Object>> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (var map:list)
        {
            var date  = map.get("orderdate");
            var newDate = sdf.format(date);
            map.remove("orderdate");
            map.put("orderdate",newDate);
        }
        return list;
    }

    @Override
    public User validateUser(String name, String password) throws AuthException {
        return userRepository.findByNameAndPassword(name,password);
    }

    @Override
    public User registerUser(String name, String password, String defaultaddress) throws AuthException {

        Integer count = userRepository.getCountByName(name);
        if(count>0)
            throw  new AuthException("User exist");
        Integer userid = userRepository.create(name,password,defaultaddress);
        return userRepository.findById(userid);
    }
}
