package lab3.controllers

import lab3.dtos.AdvertReq
import lab3.dtos.AdvertRes
import lab3.security.JWTTokenUtil
import lab3.services.AdvertService
import lab3.services.UserService
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController @RequestMapping(path = ["/advert"])
class AdvertController {

    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var advertService: AdvertService

    private fun ok(block: AdvertRes.() -> Unit) = ResponseEntity(AdvertRes().apply(block), HttpStatus.OK)

    @GetMapping(path = ["all"], produces = ["application/json"])
    fun getAll() =
        ok {
            adverts = advertService.getAll()
        }

    @GetMapping(path = ["{advertId}"], produces = ["application/json"])
    fun getAdvert(@PathVariable advertId: Long) =
        ok {
            adverts = listOf(advertService[advertId])
        }

    @GetMapping(path = ["forUser/{userId}"], produces = ["application/json"])
    fun getForUser(@PathVariable userId: Long) =
        ok {
            adverts = userService[userId].adverts
        }

    @GetMapping(path=["spec"], produces = ["application/json"])
    fun getSpecificAdverts(@RequestParam ids: String) =
        ok {
            adverts = ids.split(";").mapNotNull {
                try {
                    advertService[it.toLong()]
                } catch (e: Exception) {
                    null
                }
            }
        }

    @PostMapping(path = ["add"], consumes = ["application/json"], produces = ["application/json"])
    fun add(@Valid @RequestBody req: AdvertReq, raw: HttpServletRequest) =
        ok {
            val advert = req.toAdvert()
            if (req.mobileNumber.isBlank() || req.name.isBlank()) throw Exception("Required mobileNumber and name for advert")
            advert.user = userService loadUserByUsername (jwt decode raw)
            msg = if (advertService add advert) "Your advert was complete automoderation and sent to manual"
            else "We found a problems while moderating. Please read our rules"
        }

    @PutMapping(path = ["{advertId}"], consumes = ["application/json"], produces = ["application/json"])
    fun modify(@PathVariable advertId: Long, req: HttpEntity<String>, raw: HttpServletRequest) =
        ok {
            userService loadUserByUsername (jwt decode raw)
            advertService.modify(advertId, JSONObject(req.body).toMap().mapValues { it.toString() })
            msg = "Successfully modified"
        }

    @DeleteMapping(path = ["delete"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@RequestParam ids: String, @Valid @RequestBody req: AdvertReq, raw: HttpServletRequest) =
        ok {
            ids.split(";")
                .map { advertService[it.toLong()] }
                .filter { it.user.userId == (userService loadUserByUsername(jwt decode raw)).userId }
                .forEach(advertService::delete)
            msg = "Successfully deleted"
        }
}