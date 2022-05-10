package umg.foka.penkeats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.foka.penkeats.services.FeedbackService;
import umg.foka.penkeats.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService service;

    @PostMapping("")
    public ResponseEntity<Map<String,Object>> post(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {
        int userId = (Integer) request.getAttribute("userId");
        int permissions = (Integer) request.getAttribute("permissions");
        int orderelementid = Integer.parseInt( paramMap.get("orderelementid").toString());
        int foodid =Integer.parseInt( paramMap.get("foodid").toString());
        int rating =Integer.parseInt( paramMap.get("rating").toString());
        if(permissions >= 0 ) {
            List<Map<String, Object>> user;
            var success =  service.post( orderelementid,  userId,  foodid,  rating);
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
