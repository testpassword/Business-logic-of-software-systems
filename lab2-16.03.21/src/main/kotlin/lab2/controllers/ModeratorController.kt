package lab2.controllers

import lab2.dtos.ModerateReq
import lab2.dtos.Res
import lab2.models.Advert
import lab2.security.JWTTokenUtil
import lab2.services.AdvertService
import lab2.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController @RequestMapping(path = ["/moderate"])
class ModeratorController {

    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var advertService: AdvertService

    private fun ok(block: Res.() -> Unit) = ResponseEntity(Res().apply(block), HttpStatus.OK)

    private fun bad(block: Res.() -> Unit) = ResponseEntity(Res().apply(block), HttpStatus.BAD_REQUEST)

    //TODO: здесь проверять через токен роль пользователя
    @GetMapping(path = ["status"], produces = ["application/json"])
    fun getAdvertsByStatus(@RequestParam status: String) =
        ok {
            advertService.getAll().filter { it.status == Advert.STATUS.valueOf(status) }
        }


    @PostMapping(path = ["{advertId}"], produces = ["application/json"])
    fun setAdvertStatus(@PathVariable advertId: Long, @RequestBody req: ModerateReq) =
        ok {
            TODO()
            //advertService.changeStatus()
        }
}