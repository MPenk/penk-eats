package umg.foka.penkeats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umg.foka.penkeats.repositories.CategoryRepository;
import umg.foka.penkeats.repositories.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static umg.foka.penkeats.services.UserService.repairDate;


@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository repository;
    public List<Map<String, Object>> getById(int orderid) {
        return repository.getById(orderid);
    }

    public List<Map<String, Object>> getActive() {
        var list = repository.getActive();
        return repairDate(list);
    }
    public List<Map<String, Object>> getByIdAdd(int orderid) {
        var list =  repository.getByIdAdd(orderid);
        return repairDate(list);
    }
    public boolean post(int userid, String address, ArrayList<LinkedHashMap<String,Object>> elements) {
            return repository.post(userid,address,elements);
    }
    public boolean move(int id) {
        var order = repository.getByIdAdd(id).get(0);
        var status = Integer.parseInt(order.get("status").toString());
        if(status<0 || status>=3)
            return false;
        status++;
        return repository.move(id,status);
    }
    public boolean cancel(int id) {
        var order = repository.getByIdAdd(id).get(0);
        var status = Integer.parseInt(order.get("status").toString());
        if(status<0 || status>=3)
            return false;
        return repository.cancel(id);
    }
}
