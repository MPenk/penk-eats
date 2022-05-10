package umg.foka.penkeats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.foka.penkeats.services.CategoryService;
import umg.foka.penkeats.services.FoodService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService service;
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getCategories(){
        Map<String,Object> map = new HashMap<>();
        map.put("success", true);
        map.put("value", service.getAll());
        return  new ResponseEntity<>(map, HttpStatus.OK);
    }
    @DeleteMapping("")
    public ResponseEntity<Map<String,Object>> delete(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {

        int permissions = (Integer) request.getAttribute("permissions");
        int foodid = Integer.parseInt(paramMap.get("categoryid").toString());
        if (permissions > 1) {
            List<Map<String, Object>> user;
            var success = service.delete(foodid);
            if (!success) {
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("message", "ERR");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/restore")
    public ResponseEntity<Map<String,Object>> restore(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {

        int permissions = (Integer) request.getAttribute("permissions");
        int foodid = Integer.parseInt(paramMap.get("categoryid").toString());
        if (permissions > 1) {
            var success = service.restore(foodid);
            if (!success) {
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("message", "ERR");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("message", "Unauthorized");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("")
    public ResponseEntity<Map<String,Object>> getAll(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {

        int permissions = (Integer) request.getAttribute("permissions");
        String name = (String) paramMap.get("name");
        if(permissions > 1 ) {
            List<Map<String, Object>> user;
            var success =  service.post(name);
            if(success==0)
            {
                Map<String, Object> map = new HashMap<>();
                var options = new HashMap<String, String>();
                options.put("force","forcePost");
                map.put("success", true);
                map.put("message", "Item exist");
                map.put("options", options);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            if(success==-1){
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
