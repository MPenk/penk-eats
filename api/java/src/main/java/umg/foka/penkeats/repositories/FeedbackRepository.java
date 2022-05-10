package umg.foka.penkeats.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Repository
public class FeedbackRepository {
    private static final String SQL_GET_BY_ORDERELEMENTID = "SELECT \n" +
            "  feedbackid\n" +
            "FROM \n" +
            " feedback \n" +
            " WHERE \n" +
            " orderelementid = ?";

    private static final String SQL_INSERT = "INSERT INTO \n" +
            "  feedback\n" +
            "(\n" +
            "  orderelementid,\n" +
            "  userid,\n" +
            "  foodid,\n" +
            "  rating,\n" +
            "  date\n" +
            ")\n" +
            "VALUES (\n" +
            "  ?,\n" +
            "  ?,\n" +
            "  ?,\n" +
            "  ?,\n" +
            "  ?\n" +
            ");" ;


    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Map<String, Object>> getByorderelementId(int orderelementId) {
        return jdbcTemplate.queryForList(SQL_GET_BY_ORDERELEMENTID,orderelementId);
    }

    public boolean post(int userid, int foodid, int rating, int orderelementid) {
        try {
            jdbcTemplate.update(SQL_INSERT,orderelementid,userid,foodid,rating, new Timestamp(System.currentTimeMillis()));
            return true;
        }catch (Exception ex){
            return false;
        }
    }

}
