package testpassword.lab1.controllers;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testpassword.lab1.requests.AdvertReq;
import testpassword.lab1.responses.AdvertRes;
import testpassword.lab1.security.JWTUtil;
import testpassword.lab1.services.AdvertService;
import testpassword.lab1.services.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController @RequestMapping(path = "/advert")
public class AdvertController {

    @Autowired private JWTUtil jwtUtil;
    @Autowired private UserService userService;
    @Autowired private AdvertService advertService;

    @GetMapping(path = "getAll", produces = "application/json")
    public ResponseEntity<AdvertRes> getAll() {
        return new ResponseEntity<>(AdvertRes.builder().adverts(advertService.getAll()).build(), HttpStatus.OK);
    }

    @GetMapping(path = "get/{userId}", produces = "application/json")
    public ResponseEntity<AdvertRes> getForUser(@PathVariable long userId) {
        return userService.get(userId)
                .map(u -> new ResponseEntity<>(
                        AdvertRes.builder().adverts(u.getAdverts()).build(),
                        HttpStatus.OK)
                ).orElse(new ResponseEntity<>(
                        AdvertRes.builder().msg("Can't find user with requested id").build(),
                        HttpStatus.BAD_REQUEST));
    }

    @PostMapping(path = "getFiltered", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AdvertRes> get(@Valid @RequestBody AdvertReq req) {
        return new ResponseEntity<>(
                AdvertRes.builder().adverts(
                        Arrays.stream(req.advertsIds)
                                .mapToObj(advertService::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet()))
                        .build(),
                HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AdvertRes> add(@Valid @RequestBody AdvertReq req, HttpServletRequest rawReq) {
        return (userService.get(jwtUtil.decode(rawReq)))
                .map(u -> {
                    val advert = req.toAdvert();
                    Predicate<String> badField = x -> x == null || x.equals("");
                    if (badField.test(advert.getMobileNumber()) || badField.test(advert.getName()))
                        return new ResponseEntity<>(
                                AdvertRes.builder().msg("Use should set mobile phone or name").build(),
                                HttpStatus.BAD_REQUEST);
                    advert.setUser(u);
                    return (advertService.add(advert)) ?
                            new ResponseEntity<>(
                                    AdvertRes.builder().msg("Your advert was publish!").build(),
                                    HttpStatus.CREATED) :
                            new ResponseEntity<>(
                                    AdvertRes.builder().msg("We found a problems while moderating. Please read out rules").build(),
                                    HttpStatus.I_AM_A_TEAPOT);
                        }
                ).orElse(new ResponseEntity<>(AdvertRes.builder().msg("User didn't exist").build(), HttpStatus.UNAUTHORIZED));
    }

    @DeleteMapping(path = "delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AdvertRes> delete(@Valid @RequestBody AdvertReq req, HttpServletRequest rawReq) {
        Arrays.stream(req.getAdvertsIds())
                .mapToObj(advertService::get)
                .filter(a -> a.getUser().getUserId() == userService.get(jwtUtil.decode(rawReq)).get().getUserId())
                .forEach(advertService::delete);
        return new ResponseEntity<>(AdvertRes.builder().msg("Successfully deleted").build(), HttpStatus.OK);
    }
}