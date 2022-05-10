package umg.foka.penkeats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.foka.penkeats.exceptions.AuthException;
import umg.foka.penkeats.models.User;
import umg.foka.penkeats.services.FoodService;
import umg.foka.penkeats.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    FoodService foodService;


    @DeleteMapping("")
    public ResponseEntity<Map<String,Object>> delete(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {

        int permissions = (Integer) request.getAttribute("permissions");
        int foodid = Integer.parseInt(paramMap.get("foodid").toString());
        if (permissions > 1) {
            List<Map<String, Object>> user;
            var success = foodService.delete(foodid);
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
    public ResponseEntity<Map<String,Object>> getAll(HttpServletRequest request, @RequestBody Map<String, Object> paramMap,@RequestParam(value = "force",required=false) String forceParam) {

        int permissions = (Integer) request.getAttribute("permissions");
        String name = (String) paramMap.get("name");
        Integer categoryid = (Integer) paramMap.get("categoryid");
        String description = (String) paramMap.get("description");
        Double price = Double.parseDouble(paramMap.get("price").toString());
        String img = (String) paramMap.get("img");
        if(forceParam==null)
            forceParam="false";
        Boolean force = forceParam.compareTo("true")==0 ? true : false;
        if (permissions > 1) {
            List<Map<String, Object>> user;
            var success = foodService.post(name, categoryid, description, price, img, force);
            if (success == 0) {
                Map<String, Object> map = new HashMap<>();
                var options = new HashMap<String, String>();
                options.put("force", "forcePost");
                map.put("success", true);
                map.put("message", "Item exist");
                map.put("options", options);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            if (success == -1) {
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


}
