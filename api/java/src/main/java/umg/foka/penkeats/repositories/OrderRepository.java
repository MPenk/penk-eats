package umg.foka.penkeats.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Repository
public class OrderRepository {

    private static final String SQL_MOVE = "UPDATE orders SET status = ? WHERE orderid = ?";
    private static final String SQL_CANCEL = "UPDATE orders SET status = -1 WHERE orderid = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT rating, orderelement.foodid, orders.userid, orderelement.orderelementid,foods.name as foodname, additionalinformation, quantity,foods.price  \n" +
            "            FROM orders \n" +
            "            INNER JOIN orderelement ON orderelement.orderid = orders.orderid \n" +
            "            INNER JOIN foods ON orderelement.foodid = foods.foodid\n" +
            "            LEFT JOIN feedback ON feedback.orderelementid = orderelement.orderelementid\n" +
            "            WHERE orders.orderid=?";
    private static final String SQL_SELECT_BY_ID_ADD = "SELECT  orders.orderid,  orders.userid, orderdate, address, status, SUM(quantity * price) as sumprice \n" +
            "            FROM orders \n" +
            "            INNER JOIN orderelement ON orderelement.orderid = orders.orderid \n" +
            "            INNER JOIN foods ON orderelement.foodid = foods.foodid " +
            "            WHERE orderelement.orderid=? GROUP BY orders.orderid";
    private static final String SQL_GET = "SELECT status, orders.userid, users.name, orderelementid,foods.name as foodname, additionalinformation, quantity,foods.price \n" +
            "            FROM orderelement \n" +
            "            INNER JOIN foods ON orderelement.foodid = foods.foodid \n" +
            "            INNER JOIN orders ON orderelement.orderid = orders.orderid  \n" +
            "            INNER JOIN users ON users.userid = orders.userid ";
    private static final String SQL_POST_ORDER = "INSERT INTO orders (orderdate, userid, address, status) VALUES (?, ?, ?, 0)";
    private static final String SQL_POST_ORDERELEMENT = "INSERT INTO orderelement (orderid, foodid, additionalinformation, quantity) VALUES (?,?,?,?)";
    private static final String SQL_ACTIVE = "SELECT orders.orderid, orderdate, address, status, SUM(quantity * price) as sumprice \n" +
            "            FROM orders\n" +
            "            INNER JOIN orderelement ON orderelement.orderid = orders.orderid \n" +
            "            INNER JOIN foods ON orderelement.foodid = foods.foodid\n" +
            "            WHERE CAST (status AS INTEGER) >=0 AND CAST (status AS INTEGER) <3\n" +
            "            GROUP BY orders.orderid";
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getActive() {
        return jdbcTemplate.queryForList(SQL_ACTIVE);
    }

    public List<Map<String, Object>> get() {
        return jdbcTemplate.queryForList(SQL_GET);
    }

    public List<Map<String, Object>> getById(int id) {
        try{
            return jdbcTemplate.queryForList(SQL_SELECT_BY_ID, id);

        }
        catch(Exception e){
            return null;
        }
        }

    public List<Map<String, Object>> getByIdAdd(int id) {

            var a = jdbcTemplate.queryForList(SQL_SELECT_BY_ID_ADD, id);
        return a;
    }

    public boolean move(int id, int status) {

        try{
            jdbcTemplate.update(SQL_MOVE,status, id);
            return true;
        }catch (Exception e)
        {return false;}

    }
    public boolean cancel(int id) {

        try{
            jdbcTemplate.update(SQL_CANCEL, id);
            return true;
        }catch (Exception e)
        {return false;}

    }
    public boolean post(int userid, String address, ArrayList<LinkedHashMap<String,Object>> elements) {
        try {
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement statement = con.prepareStatement(SQL_POST_ORDER, Statement.RETURN_GENERATED_KEYS);
                            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                            statement.setInt(2, userid);
                            statement.setString(3, address);
                            return statement;
                        }
                    }, holder);

            int primaryKey = (Integer) holder.getKeys().get("orderid");

            for(LinkedHashMap<String,Object> o: elements){

                jdbcTemplate.update(SQL_POST_ORDERELEMENT,primaryKey,Integer.parseInt(o.get("foodid").toString()),o.get("additionalinformation").toString(),Integer.parseInt(o.get("quantity").toString()));
            }

            return true;
        }catch (Exception ex){
            return false;
        }
    }


}
