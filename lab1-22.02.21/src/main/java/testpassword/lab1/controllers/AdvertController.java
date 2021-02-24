package testpassword.lab1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping(path = "advert")
public class AdvertController {

    @GetMapping public ResponseEntity<String> get() {
        return new ResponseEntity<>("suc", HttpStatus.ACCEPTED);
    }

    @PostMapping public ResponseEntity<String> add() {
        return new ResponseEntity<>("suc", HttpStatus.CREATED);
    }

    @PutMapping public ResponseEntity<String> modify() {
        return new ResponseEntity<>("suc", HttpStatus.OK);
    }

    @DeleteMapping public ResponseEntity<String> delete() {
        return new ResponseEntity<>("suc", HttpStatus.OK);
    }
}