package lab2.controllers

import lab2.dtos.AdvertReq
import lab2.dtos.AdvertRes
import lab2.dtos.bad
import lab2.security.JWTTokenUtil
import lab2.services.AdvertService
import lab2.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController @RequestMapping(path = ["/advert"])
class AdvertController {

    @Autowired private lateinit var token: JWTTokenUtil
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var advertService: AdvertService

    @GetMapping(path = ["all"], produces = ["application/json"])
    fun getAll() = ok(AdvertRes(adverts = advertService.getAll()))

    @GetMapping(path = ["user/{userId}"], produces = ["application/json"])
    fun getForUser(@PathVariable userId: Long) =
        try {
            ok(AdvertRes(adverts = userService[userId].adverts))
        } catch (e: EmptyResultDataAccessException) {
            bad(AdvertRes(msg = "Can't find user with requested id"))
        }

    @PutMapping(path = ["add"], consumes = ["application/json"], produces = ["application/json"])
    fun add(@Valid @RequestBody req: AdvertReq, raw: HttpServletRequest) =
        try {
            if (req.mobileNumber.isBlank() || req.name.isBlank()) bad(AdvertRes(msg = "Use should set mobile phone or name"))
            else {
                val advert = req.toAdvert()
                advert.user = userService.loadUserByUsername(token decode raw)
                if (advertService add advert) ResponseEntity(AdvertRes(msg = "Your advert was publish!"), HttpStatus.CREATED)
                else bad(AdvertRes(msg = "We found a problems while moderating. Please read our rules"))
            }
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity(AdvertRes(msg = "User didn't exist"), HttpStatus.UNAUTHORIZED)
        }

    @DeleteMapping(path = ["delete"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@Valid @RequestBody req: AdvertReq, raw: HttpServletRequest): ResponseEntity<AdvertRes> {
        req.advertsIds
            .map(advertService::get)
            .filter { it.user.userId == userService.loadUserByUsername(token decode raw).userId }
            .forEach(advertService::delete)
        return ok(AdvertRes(msg = "Successfully deleted"))
    }
}