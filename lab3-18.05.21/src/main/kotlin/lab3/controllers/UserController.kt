package lab3.controllers

import lab3.dtos.http.UserReq
import lab3.dtos.http.UserRes
import lab3.security.JWTTokenUtil
import lab3.services.UserService
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController @RequestMapping(path = ["/user"])
class UserController {

    @Autowired private lateinit var authManager: AuthenticationManager
    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var service: UserService

    private fun ok(block: UserRes.() -> Unit) = ResponseEntity(UserRes().apply(block), HttpStatus.OK)

    private fun other(status: HttpStatus, block: UserRes.() -> Unit) = ResponseEntity(UserRes().apply(block), status)

    private fun auth(email: String, password: String): String {
        authManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
        return jwt generate email
    }

    @GetMapping(path = ["all"], produces = ["application/json"])
    fun getAll() =
        ok {
            users = service.getAll()
        }

    @GetMapping(path = ["{userId}"], produces = ["application/json"])
    fun getUser(@PathVariable userId: Long) =
        ok {
            users = listOf(service[userId])
        }

    @GetMapping(path=["spec"], produces = ["application/json"])
    fun getSpecificUsers(@RequestParam ids: String) =
        ok {
            users = ids.split(";").mapNotNull {
                try {
                    service[it.toLong()]
                } catch (e: Exception) {
                    null
                }
            }
        }

    @PostMapping(path = ["extendToken"], produces = ["application/json"])
    fun extendToken(raw: HttpServletRequest) =
        ok {
            if (jwt isExpired raw) token = jwt generate ((service loadUserByUsername (jwt decode raw)).email)
            else msg = "You already have actual token"
        }

    @PostMapping(path = ["login"], consumes = ["application/json"], produces = ["application/json"])
    fun login(@Valid @RequestBody req: UserReq) =
        ok {
            service loadUserByUsername req.email
            token = auth(req.email, req.password)
        }

    @PutMapping(path = ["register"], consumes = ["application/json"], produces = ["application/json"])
    fun register(@Valid @RequestBody req: UserReq) =
        try {
            other(HttpStatus.CONFLICT) {
                service loadUserByUsername req.email
                msg = "User ${req.email} already exist"
            }
        } catch (e: EmptyResultDataAccessException) {
            other(HttpStatus.CREATED) {
                service.add(req.email, req.password, req.name)
                token = auth(req.email, req.password)
            }
        }

    @DeleteMapping(path = ["delete"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@Valid @RequestBody req: UserReq, raw: HttpServletRequest) =
        ok {
            val email = (service loadUserByUsername (jwt decode raw)).email
            auth(email, req.password)
            service delete email
            msg = "Successfully delete account"
        }

    @PutMapping(path = ["modify"], consumes = ["application/json"], produces = ["application/json"])
    fun modify(@Valid @RequestBody req: UserReq, raw: HttpServletRequest) =
        ok {
            val email = (service loadUserByUsername (jwt decode raw)).email
            auth(email, req.password)
            service.modify(email, JSONObject(req.payload).toMap().mapValues { it.toString() })
        }

    @PatchMapping(path = ["reset"], consumes = ["application/json"], produces = ["application/json"])
    fun resetPassword(@Valid @RequestBody req: UserReq) =
        ok {
            msg = if (service resetPassword req.email)
                "Temp password sent to your email" else "Error on server while reseting password, try later"
        }
}