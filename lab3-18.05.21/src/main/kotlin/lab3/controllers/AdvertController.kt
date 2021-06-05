package lab3.controllers

import lab3.dtos.http.AdvertReq
import lab3.dtos.http.AdvertRes
import lab3.models.Advert
import lab3.security.JWTTokenUtil
import lab3.services.AdvertService
import lab3.services.UserService
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
    fun getAll() = ok { adverts = advertService.getAll() }

    @GetMapping(path = ["{advertId}"], produces = ["application/json"])
    fun getAdvert(@PathVariable advertId: Long) = ok { adverts = listOf(advertService[advertId]) }

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
            println("a norm $req")
            if (req.mobileNumber.isNullOrBlank() || req.name.isNullOrBlank()) throw Exception("Required mobileNumber and name for advert")
            val advert = req.fillAdvert(Advert())
            advert.user = userService loadUserByUsername (jwt decode raw)
            msg = if (advertService add advert) "Your advert was complete automoderation and sent to manual"
            else "We found a problems while moderating. Please read our rules"
        }

    @PutMapping(path = ["{advertId}"], consumes = ["application/json"], produces = ["application/json"])
    fun modify(@PathVariable advertId: Long, @RequestBody req: AdvertReq, raw: HttpServletRequest) =
        ok {
            userService loadUserByUsername (jwt decode raw)
            advertService.modify(advertId, req)
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