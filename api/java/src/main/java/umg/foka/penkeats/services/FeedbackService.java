package umg.foka.penkeats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umg.foka.penkeats.repositories.CategoryRepository;
import umg.foka.penkeats.repositories.FeedbackRepository;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class FeedbackService {

    @Autowired
    FeedbackRepository repository;

    public List<Map<String, Object>> getByorderelementId(int orderelementId){
        return repository.getByorderelementId(orderelementId);
    }

    public boolean post(int orderelementid, int userid, int foodid, int rating) {
        var list= getByorderelementId(orderelementid);
        if(list.size()>0)
            return false;
        return repository.post(userid,foodid,rating, orderelementid);

    }

}
