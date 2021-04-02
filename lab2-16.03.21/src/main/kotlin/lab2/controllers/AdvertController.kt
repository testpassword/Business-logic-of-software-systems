package lab2.controllers

import com.fasterxml.jackson.databind.JsonMappingException
import lab2.dtos.AdvertReq
import lab2.dtos.AdvertRes
import lab2.security.JWTTokenUtil
import lab2.services.AdvertService
import lab2.services.UserService
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpEntity
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

    fun ok(block: AdvertRes.() -> Unit) = ResponseEntity(AdvertRes().apply(block), HttpStatus.OK)

    fun bad(block: AdvertRes.() -> Unit) = ResponseEntity(AdvertRes().apply(block), HttpStatus.BAD_REQUEST)

    @GetMapping(path = ["all"], produces = ["application/json"])
    fun getAll() = ok(AdvertRes(adverts = advertService.getAll()))

    @GetMapping(path = ["user/{userId}"], produces = ["application/json"])
    fun getForUser(@PathVariable userId: Long) =
        try {
            ok {
                adverts = userService[userId].adverts
            }
        } catch (e: EmptyResultDataAccessException) {
            bad {
                msg = "Can't find user with requested id"
            }
        }

    @GetMapping(path=["spec"], produces = ["application/json"])
    fun getSpecificAdverts(@RequestParam ids: String) =
        try {
            ok {
                adverts = ids.split(";")
                    .map {
                        try {
                            advertService[it.toLong()]
                        } catch (e: Exception) {
                            null
                        }
                    }.filterNotNull()
            }
        } catch (e: Exception) {
            bad {
                msg = "Unexpected exception found: $e"
            }
        }

    @PostMapping(path = ["add"], consumes = ["application/json"], produces = ["application/json"])
    fun add(@Valid @RequestBody req: AdvertReq, raw: HttpServletRequest) =
        try {
            if (req.mobileNumber.isBlank() || req.name.isBlank())
                bad {
                    msg = "Use should set mobile phone or name"
                }
            else {
                val advert = req.toAdvert()
                advert.user = userService.loadUserByUsername(token decode raw)
                if (advertService add advert) ResponseEntity(AdvertRes(msg = "Your advert was publish!"), HttpStatus.CREATED)
                else bad {
                    msg = "We found a problems while moderating. Please read our rules"
                }
            }
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity(AdvertRes(msg = "User didn't exist"), HttpStatus.UNAUTHORIZED)
        }

    @PutMapping(path = ["modify"], consumes = ["application/json"], produces = ["application/json"])
    fun modity(req: HttpEntity<String>, raw: HttpServletRequest) =
        try {
            ok {
                userService loadUserByUsername (token decode raw)
                val modifiedFields = JSONObject(req.body).toMap().mapValues { it.toString() }
                if (modifiedFields.containsKey("advertId").not()) throw JsonMappingException("")
                advertService modify modifiedFields
                msg = "Successfully modified"
            }
        } catch (e: Exception) {
            /*TODO: использовать общий механизм перехвата исключений определённого типа
            EmptyResultDataAccessException
            IllegalArgumentException
            все остальные
            json ключ отсутсвует
             */
        }

    @DeleteMapping(path = ["delete"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@Valid @RequestBody req: AdvertReq, raw: HttpServletRequest): ResponseEntity<AdvertRes> =
        try {
            ok {
                req.advertsIds
                    .map(advertService::get)
                    .filter { it.user.userId == userService.loadUserByUsername(token decode raw).userId }
                    .forEach(advertService::delete)
                msg = "Successfully deleted"
            }
        } catch (e: Exception) {
            bad {
                msg = when (e) {
                    is IllegalArgumentException -> "You should specify token for this operation"
                    is EmptyResultDataAccessException -> "Owner of advert didn't exist"
                    else -> "Unexpected exception found: $e"
                }
            }
        }
}