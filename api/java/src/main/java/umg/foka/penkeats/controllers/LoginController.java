package umg.foka.penkeats.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.foka.penkeats.Constants;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;
import umg.foka.penkeats.services.UserService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("")
    public ResponseEntity<Map<String,Object>> login(@RequestBody Map<String, Object> userMap) {

        String name = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        User user = null;
        try{
             user = userService.validateUser(name,password);
        }
        catch (AuthException e){
            Map<String,Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", e.getMessage());
            return  new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("success", true);
        map.put("token", generateToken(user));
        return  new ResponseEntity<>(map, HttpStatus.OK);
    }

    private String generateToken(User user){
        long timestamp = System.currentTimeMillis();
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Constants.API_SECRET_KEY));
        String token = Jwts.builder().signWith(key)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userid", user.getUserid())
                .claim("name", user.getName())
                .claim("permissions", user.getPremissions())
                .claim("defaultaddress", user.getDefaultaddress())
                .compact();
        return token;
    }
}
