package umg.foka.penkeats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;
import umg.foka.penkeats.services.CategoryService;
import umg.foka.penkeats.services.FoodService;
import umg.foka.penkeats.services.UserService;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @Autowired
    FoodService foodService;

    @GetMapping("/category")
    public ResponseEntity<Map<String, Object>> getCategories(){
        Map<String,Object> map = new HashMap<>();
        map.put("success", true);
        map.put("value", categoryService.get());
        return  new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String,Object>> newUser(@RequestBody Map<String, Object> userMap) {

        String name = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        String address = (String) userMap.get("address");
        try{
            User user = userService.registerUser(name,password,address);
        }
        catch (AuthException e){
            Map<String,Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", e.getMessage());
            return  new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("success", true);
        return  new ResponseEntity<>(map, HttpStatus.OK);

    }

    @GetMapping("/food")
    public ResponseEntity<Map<String, Object>> getFoods(){
        Map<String,Object> map = new HashMap<>();
        map.put("success", true);
        map.put("value", foodService.getAll());
        return  new ResponseEntity<>(map, HttpStatus.OK);
    }

}
