package lab2.controllers

import io.jsonwebtoken.JwtException
import org.springframework.security.core.AuthenticationException
import lab2.dtos.ModerateReq
import lab2.dtos.Res
import lab2.models.Advert
import lab2.models.RoleException
import lab2.models.User
import lab2.security.JWTTokenUtil
import lab2.services.AdvertService
import lab2.services.UserService
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

    private fun ok(raw: HttpServletRequest, block: Res.() -> Unit): ResponseEntity<Res> {
        if ((userService loadUserByUsername (jwt decode raw)).role != User.ROLE.MODERATOR) throw RoleException()
        return ResponseEntity(Res().apply(block), HttpStatus.OK)
    }

    private fun bad(block: Res.() -> Unit) = ResponseEntity(Res().apply(block), HttpStatus.BAD_REQUEST)

    @GetMapping(path = ["get_by_status"], produces = ["application/json"])
    fun getAdvertsByStatus(@RequestParam status: String, raw: HttpServletRequest) =
        ok(raw) {
            advertService.getAll(Advert.STATUS.valueOf(status))
        }

    @PostMapping(path = ["change/{advertId}"], produces = ["application/json"])
    fun setAdvertStatus(@PathVariable advertId: Long, @RequestBody req: ModerateReq, raw: HttpServletRequest) =
        ok(raw) {
            if (req.status in listOf(Advert.STATUS.DECLINED, Advert.STATUS.ON_MODERATION) && req.comment.isBlank())
                throw DataFormatException()
            advertService.changeStatus(advertId, req.status, req.comment)
            msg = "Status changed correct"
        }

    @ExceptionHandler(Exception::class) fun handleErrors(req: HttpServletRequest, e: Exception) =
        bad {
            msg = when (e) {
                is RoleException -> "You should be ${User.ROLE.MODERATOR} for this operation"
                is JwtException -> "Bad token or didn't presented"
                is DataFormatException -> "You must specify why the ad wasn't approved"
                is AuthenticationException -> "This url allowed only for moderators"
                else -> """
                    Unexpected exception, try later or contact support with this message
                    $e
                    $req
                    """.trimIndent()
            }
        }
}