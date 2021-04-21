package lab2.controllers

import io.jsonwebtoken.JwtException
import org.springframework.security.core.AuthenticationException
import lab2.dtos.ModerateReq
import lab2.dtos.Res
import lab2.models.Advert
import lab2.security.JWTTokenUtil
import lab2.services.AdvertService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.zip.DataFormatException
import javax.servlet.http.HttpServletRequest

@RestController @RequestMapping(path = ["/moderate"])
class ModeratorController {

    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var advertService: AdvertService
    private val roleException = object: AuthenticationException("") {}

    private fun ok(raw: HttpServletRequest, block: Res.() -> Unit): ResponseEntity<Res> {
        if ("MODERATOR" !in jwt getRoles (jwt resolve raw)!!) throw roleException
        return ResponseEntity(Res().apply(block), HttpStatus.OK)
    }

    private fun bad(block: Res.() -> Unit) = ResponseEntity(Res().apply(block), HttpStatus.BAD_REQUEST)

    @GetMapping(path = ["status"], produces = ["application/json"])
    fun getAdvertsByStatus(@RequestParam status: String, raw: HttpServletRequest) =
        ok(raw) {
            if ("MODERATOR" !in jwt getRoles (jwt resolve raw)!!) throw roleException
            advertService.getAll(Advert.STATUS.valueOf(status))
        }

    @PostMapping(path = ["{advertId}"], produces = ["application/json"])
    fun setAdvertStatus(@PathVariable advertId: Long, @RequestBody req: ModerateReq, raw: HttpServletRequest) =
        ok(raw) {
            if (req.status in listOf(Advert.STATUS.DECLINED, Advert.STATUS.ON_MODERATION) && req.comment.isBlank())
                throw DataFormatException()
            advertService.changeStatus(advertId, req.status, req.comment)
            msg = "Status changed correct"
        }

    @ExceptionHandler(Exception::class)
    fun handleErrors(req: HttpServletRequest, e: Exception) =
        bad {
            msg = when (e) {
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