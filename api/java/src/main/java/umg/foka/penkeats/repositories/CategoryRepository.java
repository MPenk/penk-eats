package umg.foka.penkeats.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class CategoryRepository {

    private static final String SQL_SELECT = "SELECT \n" +
            "            categoryid,\n" +
            "            name\n" +
            "           FROM categories WHERE deleted is not true";
    private static final String SQL_SELECT_ALL = "SELECT \n" +
            "            categoryid,\n" +
            "            name, deleted\n" +
            "           FROM categories";
    private static final String SQL_SELECT_BY_NAME = "SELECT categoryid FROM categories WHERE name=?";
    private static final String SQL_POST = "INSERT INTO categories (name) VALUES (?)";
    private static final String SQL_DELETE = "UPDATE categories SET deleted = true WHERE categoryid = ?";
    private static final String SQL_RESTORE = "UPDATE categories SET deleted = false WHERE categoryid = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAll() {
        return jdbcTemplate.queryForList(SQL_SELECT_ALL);
    }
    public List<Map<String, Object>> get() {
        return jdbcTemplate.queryForList(SQL_SELECT);
    }

    public List<Map<String, Object>> getByName(String name) {
        return jdbcTemplate.queryForList(SQL_SELECT_BY_NAME,name);
    }
    public boolean post(String name) {
        try {
            jdbcTemplate.update(SQL_POST,name);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean delete(int categoryid) {
        try {
            jdbcTemplate.update(SQL_DELETE,categoryid);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
    public boolean restore(int categoryid) {
        try {
            jdbcTemplate.update(SQL_RESTORE,categoryid);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
