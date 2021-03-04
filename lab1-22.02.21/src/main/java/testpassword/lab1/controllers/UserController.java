package testpassword.lab1.controllers;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import testpassword.lab1.requests.UserReq;
import testpassword.lab1.responses.UserRes;
import testpassword.lab1.security.JWTUtil;
import testpassword.lab1.services.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController @RequestMapping(path = "/user")
public class UserController {

    @Autowired private AuthenticationManager auth;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private UserService service;

    @GetMapping(path = "all", produces = "application/json")
    public ResponseEntity<UserRes> getAll() {
        return new ResponseEntity<>(
                UserRes.builder().users(service.getAll()).build(),
                HttpStatus.OK);
    }

    @GetMapping(path = "{userId}", produces = "application/json")
    public ResponseEntity<UserRes> getUser(@PathVariable long userId) {
        return service.get(userId)
                .map(u -> new ResponseEntity<>(
                        UserRes.builder().users(Collections.singletonList(u)).build(),
                        HttpStatus.OK)
                ).orElse(new ResponseEntity<>(
                        UserRes.builder().msg("User with this id didn't exist").build(),
                        HttpStatus.BAD_REQUEST));
    }

    @PostMapping(path = "login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserRes> login(@Valid @RequestBody UserReq req) {
        return service.get(req.email)
                .map(u -> {
                    try {
                        auth.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
                        return new ResponseEntity<>(
                                UserRes.builder()
                                        .token(jwtUtil.generateToken(u.getEmail(), Collections.singletonList("USER")))
                                        .build(),
                                HttpStatus.ACCEPTED);
                    } catch (AuthenticationException e) {
                        return new ResponseEntity<>(
                                UserRes.builder().msg("Password incorrect").build(),
                                HttpStatus.BAD_REQUEST);
                    }
                }).orElse(new ResponseEntity<>(
                        UserRes.builder().msg("User didn't exist. Check email and password").build(),
                        HttpStatus.BAD_REQUEST));
    }

    @PutMapping(path = "register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserRes> register(@Valid @RequestBody UserReq req) {
        return service.get(req.email)
                .map(u -> new ResponseEntity<>(
                        UserRes.builder().msg("User already exist").build(),
                        HttpStatus.CONFLICT)
                ).orElseGet(() -> {
                    val u = service.add(req.email, req.password, req.name).get();
                    auth.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
                    return new ResponseEntity<>(
                            UserRes.builder()
                                    .token(jwtUtil.generateToken(u.getEmail(), Collections.singletonList("USER")))
                                    .build(),
                            HttpStatus.CREATED);
                });
    }

    @PatchMapping(path = "modify", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserRes> modify(@Valid @RequestBody UserReq req, HttpServletRequest rawReq) {
        return (service.get(jwtUtil.decode(rawReq)))
                .map(u -> {
                    if (req.email != null)
                        if (service.exist(req.email))
                            return new ResponseEntity<>(
                                    UserRes.builder().msg("Mail is busy").build(),
                                    HttpStatus.CONFLICT);
                        else u.setEmail(req.email);
                    Optional.ofNullable(req.name).ifPresent(u::setName);
                    Optional.ofNullable(req.password).ifPresent(u::setPassword);
                    service.save(u);
                    return new ResponseEntity<>(
                            UserRes.builder().token(jwtUtil.generateToken(req.email, Collections.singletonList("USER"))).build(),
                            HttpStatus.ACCEPTED);
                }).orElse(new ResponseEntity<>(
                        UserRes.builder().msg("User didn't exist").build(),
                        HttpStatus.UNAUTHORIZED));
    }

    @DeleteMapping(path = "delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserRes> delete(@Valid @RequestBody UserReq req, HttpServletRequest rawReq) {
        return (service.get(jwtUtil.decode(rawReq)))
                .map(u -> {
                    try {
                        System.out.println(u.getEmail());
                        auth.authenticate(new UsernamePasswordAuthenticationToken(u.getEmail(), req.password));
                        service.delete(u.getEmail());
                        return new ResponseEntity<>(
                                UserRes.builder().msg("Successful delete account").build(),
                                HttpStatus.OK);
                    } catch (AuthenticationException e) {
                        return new ResponseEntity<>(
                                UserRes.builder().msg("Bad password").build(),
                                HttpStatus.UNAUTHORIZED);
                    }
                }).orElse(new ResponseEntity<>(
                        UserRes.builder().msg("Wrong session token").build(),
                        HttpStatus.UNAUTHORIZED));
    }

    @PostMapping(path = "restore", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserRes> restore(@Valid @RequestBody UserReq req) {
        return (service.exist(req.email) && service.restorePassword(req.email)) ?
                new ResponseEntity<>(UserRes.builder().msg("Restore email was send").build(), HttpStatus.OK) :
                new ResponseEntity<>(UserRes.builder().msg("Internal error, try later").build(), HttpStatus.BAD_REQUEST);
    }
}