package lab3.controllers

import lab3.dtos.http.ModerateReq
import lab3.dtos.http.ModeratorRes
import lab3.models.Advert
import lab3.models.RoleException
import lab3.models.User
import lab3.security.JWTTokenUtil
import lab3.services.AdvertService
import lab3.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.zip.DataFormatException
import javax.servlet.http.HttpServletRequest

@RestController @RequestMapping(path = ["/moderate"])
class ModeratorController {

    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var advertService: AdvertService

    private fun ok(raw: HttpServletRequest, block: ModeratorRes.() -> Unit): ResponseEntity<ModeratorRes> {
        if ((userService loadUserByUsername (jwt decode raw)).role != User.ROLE.MODERATOR) throw RoleException()
        return ResponseEntity(ModeratorRes().apply(block), HttpStatus.OK)
    }

    @GetMapping(path = ["get_by_status"], produces = ["application/json"])
    fun getAdvertsByStatus(@RequestParam status: String, raw: HttpServletRequest) =
        ok(raw) {
            adverts = advertService.getAll(Advert.STATUS.valueOf(status))
        }

    @PostMapping(path = ["change/{advertId}"], produces = ["application/json"])
    fun setAdvertStatus(@PathVariable advertId: Long, @RequestBody req: ModerateReq, raw: HttpServletRequest) =
        ok(raw) {
            if (req.status in listOf(Advert.STATUS.DECLINED, Advert.STATUS.ON_MODERATION) && req.comment.isBlank())
                throw DataFormatException()
            advertService.changeStatus(advertId, req.status, req.comment)
            msg = "Status changed correct"
        }
}