package umg.foka.penkeats.repositories;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


@Repository
public class UserRepository implements  IUserRepository{

    private static final String SQL_PROMOTE = "UPDATE users SET permissions = ? WHERE userid=?";
    private static final String SQL_DEGRADE = "UPDATE users SET permissions = 0 WHERE userid=?";
    private static final String SQL_CREATE = "INSERT INTO users (name, password, defaultaddress, permissions) VALUES (?, ?, ?, 0)";
    private static final String SQL_COUNT_BY_NAME = "SELECT COUNT(*) FROM users WHERE name=?";
    private static final String SQL_FIND_BY_ID = "SELECT userid, name, password, permissions, defaultaddress FROM users WHERE userid=?";
    private static final String SQL_FIND_BY_NAME = "SELECT userid, name, password, permissions, defaultaddress FROM users WHERE name=?";
    private static  final String SQL_SELECT_ORDERS_BY_ID = "SELECT orders.orderid, orderdate, address, status, SUM(quantity * price) as sumprice \n" +
            "            FROM orders \n" +
            "            INNER JOIN orderelement ON orderelement.orderid = orders.orderid \n" +
            "            INNER JOIN foods ON orderelement.foodid = foods.foodid\n" +
            "            WHERE userid=? \n" +
            "            GROUP BY orders.orderid";
    private static  final String SQL_SELECT_BY_ID = "SELECT userid, name, permissions, defaultaddress FROM users WHERE userid=?";
    private static  final String SQL_SELECT_ALL = "SELECT userid, name, permissions, defaultaddress FROM users";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String name, String password, String defaultaddress) throws AuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        System.out.println(hashedPassword);
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection ->{
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, hashedPassword);
                ps.setString(3, defaultaddress);
                return ps;
            },keyHolder);
             return (Integer) keyHolder.getKeys().get("userid");
        }catch (Exception e)
        {
            throw new AuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByNameAndPassword(String name, String password) throws AuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_NAME, userRowMapper, new Object[]{name});
            if(!BCrypt.checkpw(password, user.getPassword()))
                throw  new AuthException("Invalid name/password");
            return user;
        }
        catch (EmptyResultDataAccessException e)
        {
            throw  new AuthException("Invalid name/password");
        }
    }

    @Override
    public Integer getCountByName(String name) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_NAME, Integer.class, new Object[]{name});
    }

    @Override
    public User findById(Integer userid) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper ,new Object[]{userid});
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) ->{
        return new User(rs.getInt("userid"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("defaultaddress"),
                rs.getString("permissions"));
    });

    public List<Map<String, Object>> getOrdersById(int id) {
        return jdbcTemplate.queryForList(SQL_SELECT_ORDERS_BY_ID, id);
    }

    public List<Map<String, Object>> getUserById(int id) {
        return jdbcTemplate.queryForList(SQL_SELECT_BY_ID, id);
    }
    public List<Map<String, Object>> getAll() {
        return jdbcTemplate.queryForList(SQL_SELECT_ALL);
    }
    public boolean promote(int id, int perm) {

        try{
            jdbcTemplate.update(SQL_PROMOTE, perm  ,id);
            return true;
        }catch (Exception e)
        {return false;}

    }

    public boolean degrade(int id) {

        try{
            jdbcTemplate.update(SQL_DEGRADE,id);
            return true;
        }catch (Exception e)
        {return false;}

    }
}
