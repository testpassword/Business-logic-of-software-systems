package lab2.controllers

import io.jsonwebtoken.JwtException
import lab2.dtos.AdminReq
import lab2.dtos.Res
import lab2.models.RoleException
import lab2.models.User
import lab2.security.JWTTokenUtil
import lab2.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController @RequestMapping(path = ["/admin"])
class AdminController {

    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var service: UserService

    private fun ok(raw: HttpServletRequest, block: Res.() -> Unit): ResponseEntity<Res> {
        if ((service loadUserByUsername (jwt decode raw)).role != User.ROLE.ADMIN) throw RoleException()
        return ResponseEntity(Res().apply(block), HttpStatus.OK)
    }

    private fun bad(block: Res.() -> Unit) = ResponseEntity(Res().apply(block), HttpStatus.BAD_REQUEST)

    @DeleteMapping(path = ["delete/{userId}"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@PathVariable userId: Long, @Valid @RequestBody req: AdminReq, raw: HttpServletRequest) =
        ok(raw) {
            service delete service[userId].email
            msg = "You successfully delete user $userId"
        }

    @PatchMapping(path = ["change_status/{userId}"], consumes = ["application/json"], produces = ["application/json"])
    fun changeStatus(@PathVariable userId: Long, @Valid @RequestBody req: AdminReq, raw: HttpServletRequest) =
        ok(raw) {
            msg = req.userStatus?.let {
                service save service[userId].apply { status = req.userStatus }
                "Status of user $userId changed to ${req.userStatus}"
            } ?: throw IllegalArgumentException()
        }

    @PatchMapping(path = ["change_role/{userId}"], consumes = ["application/json"], produces = ["application/json"])
    fun changeRole(@PathVariable userId: Long, @Valid @RequestBody req: AdminReq, raw: HttpServletRequest) =
        ok(raw) {
            msg = req.userRole?.let {
                service save service[userId].apply { role = req.userRole }
                "Role of user $userId changed to ${req.userRole}"
            } ?: throw IllegalArgumentException()
        }

    @ExceptionHandler(Exception::class) fun handleErrors(req: HttpServletRequest, e: Exception) =
        bad {
            msg = when (e) {
                is RoleException -> "You should be ${User.ROLE.ADMIN} for this operation"
                is IllegalArgumentException -> "You should add param for change"
                is JwtException -> "Bad token or didn't presented"
                is EmptyResultDataAccessException -> "Entity didn't exist: $e"
                else -> """
                    Unexpected exception, try later or contact support with this message
                    ${e.stackTraceToString()}
                    $req
                    """.trimIndent()
            }
        }
}