package umg.foka.penkeats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umg.foka.penkeats.repositories.CategoryRepository;
import umg.foka.penkeats.repositories.FoodRepository;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    public List<Map<String, Object>> getAll(){
        return repository.getAll();
    }
    public List<Map<String, Object>> get(){
        return repository.get();
    }

    public boolean delete(int categoryid) {

        return repository.delete(categoryid);
    }
    public boolean restore(int categoryid) {

        return repository.restore(categoryid);
    }
    public int post(String name) {

        var foodsWithSameName = repository.getByName(name);

        if(foodsWithSameName.size()<1) {

            return repository.post(name)?1:-1;
        }else{
            return 0;
        }
    }

}
