package testpassword.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import testpassword.lab1.models.User;
import testpassword.lab1.requests.UserDTO;
import testpassword.lab1.security.JWTUtil;
import testpassword.lab1.services.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController @RequestMapping(path = "/user")
public class UserController {

    private final AuthenticationManager auth;
    private final JWTUtil jwtUtil;
    private final UserService service;

    @Autowired public UserController(AuthenticationManager auth, JWTUtil jwt, UserService service) {
        this.auth = auth;
        this.jwtUtil = jwt;
        this.service = service;
    }

    @GetMapping public ResponseEntity<String> login(@Valid @RequestBody UserDTO req) {
        auth.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
        return new ResponseEntity<>(jwtUtil.generateToken(req.email, List.of("USER")), HttpStatus.ACCEPTED);
    }

    @PostMapping public ResponseEntity<String> register(@Valid @RequestBody UserDTO req) {
        if (service.isExist(req.email)) return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        else {
            service.add(req.email, req.password, req.name);
            auth.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
            return new ResponseEntity<>(jwtUtil.generateToken(req.email, List.of("USER")), HttpStatus.ACCEPTED);
        }
    }

    @PutMapping public ResponseEntity<String> modify(@Valid @RequestBody UserDTO req, HttpServletRequest rawReq) {
        User u = service.get(jwtUtil.resolveToken(rawReq));
        if (req.email != null)
            if (service.isExist(req.email)) return new ResponseEntity<>("Mail is busy", HttpStatus.BAD_REQUEST);
            else u.setEmail(req.email);
        if (req.name != null) u.setName(req.name);
        if (req.password != null) u.setPassword(req.password);
        return new ResponseEntity<>(jwtUtil.generateToken(req.email, List.of("USER")), HttpStatus.ACCEPTED);
    }

    @DeleteMapping public ResponseEntity<String> delete(@Valid @RequestBody UserDTO req, HttpServletRequest rawReq) {
        User u = service.get(jwtUtil.resolveToken(rawReq));
        service.delete(req.email, req.password);
        return new ResponseEntity<>("Successful delete account", HttpStatus.OK);
    }
}