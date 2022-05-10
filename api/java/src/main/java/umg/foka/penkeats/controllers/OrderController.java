package umg.foka.penkeats.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.services.CategoryService;
import umg.foka.penkeats.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService service;

    @GetMapping("/active")
    public ResponseEntity<Map<String,Object>> userActive(HttpServletRequest request) {
        int permissions = (Integer) request.getAttribute("permissions");
        if(permissions >= 0 ) {
            List<Map<String, Object>> data;

            data = service.getActive();
                Map<String, Object> map = new HashMap<>();
                map.put("success", true);
                map.put("value", data);
                return new ResponseEntity<>(map, HttpStatus.OK);

        }
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("message", "Unauthorized");
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> userById(HttpServletRequest request, @PathVariable(value="id") int id) {
        int userId = (Integer) request.getAttribute("userId");
        int permissions = (Integer) request.getAttribute("permissions");
        if(permissions >= 0 ) {
            List<Map<String, Object>> data;

            data = service.getById(id);
            var dataAdd = service.getByIdAdd(id);
            if ((int) data.get(0).get("userid") == userId || permissions >= 1) {
                Map<String, Object> map = new HashMap<>();
                map.put("success", true);
                map.put("value", data);
                map.put("add", dataAdd);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Map<String,Object>> cancel(HttpServletRequest request, @PathVariable(value="id") int id) {
        int userId = (Integer) request.getAttribute("userId");
        int permissions = (Integer) request.getAttribute("permissions");
        if(permissions >= 0 ) {
            var order = service.getByIdAdd(id).get(0);
            var useridfromOrder = Integer.parseInt(order.get("userid").toString());
            if(useridfromOrder==userId || permissions >=1 ) {
                var success = service.cancel(id);
                if (!success) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("success", false);
                    map.put("message", "ERR");
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("success", true);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("message", "Unauthorized");
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);

    }
    @PostMapping("/{id}/move")
    public ResponseEntity<Map<String,Object>> move(HttpServletRequest request, @PathVariable(value="id") int id) {
        int userId = (Integer) request.getAttribute("userId");
        int permissions = (Integer) request.getAttribute("permissions");
        if(permissions >= 1 ) {
            var order = service.getByIdAdd(id).get(0);
            var useridfromOrder = Integer.parseInt(order.get("userid").toString());

                var success = service.move(id);
                if (!success) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("success", false);
                    map.put("message", "ERR");
                    return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("success", true);
                return new ResponseEntity<>(map, HttpStatus.OK);
        }
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);

    }

    @PostMapping("")
    public ResponseEntity<Map<String,Object>> post(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {
        int userId = (Integer) request.getAttribute("userId");
        int permissions = (Integer) request.getAttribute("permissions");
        String address = (String) paramMap.get("address");
        ArrayList< LinkedHashMap<String,Object>> elements = (ArrayList< LinkedHashMap<String,Object>>) paramMap.get("elements");
        if(permissions >= 0 ) {
            List<Map<String, Object>> user;
            var success =  service.post(userId,address,elements);
            if (!success) {
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("message", "ERR");
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

}
