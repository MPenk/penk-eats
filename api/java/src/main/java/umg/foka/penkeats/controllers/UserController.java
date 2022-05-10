package umg.foka.penkeats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;
import umg.foka.penkeats.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/all")
    public ResponseEntity<Map<String,Object>> getAll(HttpServletRequest request) {

        int permissions = (Integer) request.getAttribute("permissions");
        if(permissions > 1 ) {
            List<Map<String, Object>> user;
            try {
                user = userService.getAll();
            } catch (AuthException e) {
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("message", e.getMessage());
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            map.put("value", user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> userById(HttpServletRequest request, @PathVariable(value="id") int id) {

        int userId = (Integer) request.getAttribute("userId");
        int permissions = (Integer) request.getAttribute("permissions");
        if(id==userId || permissions > 1 ) {
            List<Map<String, Object>> user;
            try {
                user = userService.getUserById(id);
            } catch (AuthException e) {
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("message", e.getMessage());
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            map.put("value", user);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/{id}/promote")
    public ResponseEntity<Map<String,Object>> promote(HttpServletRequest request, @PathVariable(value="id") int id) {

        int permissions = (Integer) request.getAttribute("permissions");
        if(permissions > 1 ) {
                var success = userService.promote(id);
            if(!success){
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/{id}/degrade")
    public ResponseEntity<Map<String,Object>> degrade(HttpServletRequest request, @PathVariable(value="id") int id) {

        int permissions = (Integer) request.getAttribute("permissions");
        if(permissions > 1 ) {
            var success = userService.degrade(id);
            if(!success){
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<Map<String,Object>> ordersById(HttpServletRequest request, @PathVariable(value="id") int id) {

        int userId = (Integer) request.getAttribute("userId");
        int permissions = (Integer) request.getAttribute("permissions");
        if(id==userId || permissions > 1 ) {
            List<Map<String, Object>> orders;
            try {
                orders = userService.getOrdersById(id);
            } catch (AuthException e) {
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("message", e.getMessage());
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            map.put("value", orders);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

    }

}
