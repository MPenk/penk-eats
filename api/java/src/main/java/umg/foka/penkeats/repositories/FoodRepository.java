package umg.foka.penkeats.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class FoodRepository {

    private static final String SQL_SELECT_ALL = "SELECT foods.foodid,foods.name as name ,description,price,img,categories.name as category, avg(rating) as rating\n" +
            "FROM foods \n" +
            "INNER JOIN categories ON foods.categoryid = categories.categoryid\n" +
            " LEFT JOIN feedback ON foods.foodid = feedback.foodid  \n" +
            "WHERE foods.deleted is not true AND categories.deleted is not true\n" +
            "GROUP BY foods.foodid,categories.name,foods.name,description";
    private static final String SQL_SELECT_BY_NAME = "SELECT foodid FROM foods WHERE name=?";
    private static final String SQL_POST = "INSERT INTO foods (name, categoryid, description, price, img ) VALUES (?,?, ?, ?, ?)";
    private static final String SQL_DELETE = "UPDATE foods SET deleted = true WHERE foodid = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAll() {
        return jdbcTemplate.queryForList(SQL_SELECT_ALL);
    }
    public List<Map<String, Object>> getByName(String name) {
        return jdbcTemplate.queryForList(SQL_SELECT_BY_NAME,name);
    }
    public boolean post(String name, Integer catid, String desc, Double price, String img) {
        try {
            jdbcTemplate.update(SQL_POST,name,catid,desc,price,img);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean delete(int foodid) {
        try {
            jdbcTemplate.update(SQL_DELETE,foodid);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
