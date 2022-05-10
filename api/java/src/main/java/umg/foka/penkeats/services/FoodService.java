package umg.foka.penkeats.services;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;
import umg.foka.penkeats.repositories.FoodRepository;
import umg.foka.penkeats.repositories.IUserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class FoodService {

    @Autowired
    FoodRepository foodRepository;

    public List<Map<String, Object>> getAll(){
        return foodRepository.getAll();
    }

    public int post(String name, Integer categoryid, String description, Double price, String img, Boolean force) {

        var foodsWithSameName = foodRepository.getByName(name);

        if(foodsWithSameName.size()<1 || force) {
            return foodRepository.post(name, categoryid, description, price, img)?1:-1;
        }else{
            return 0;
        }
    }

    public boolean delete(int foodid) {

            return foodRepository.delete(foodid);
    }

  /*  public User registerUser(String name, String password, String defaultaddress) throws AuthException {

        Integer count = userRepository.getCountByName(name);
        if(count>0)
            throw  new AuthException("User exist");
        Integer userid = userRepository.create(name,password,defaultaddress);
        return userRepository.findById(userid);
    }*/


}
