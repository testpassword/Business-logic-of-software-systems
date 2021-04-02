package lab2.controllers

import lab2.dtos.UserReq
import lab2.dtos.UserRes
import lab2.security.JWTTokenUtil
import lab2.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController @RequestMapping(path = ["/user"])
class UserController {

    @Autowired private lateinit var auth: AuthenticationManager
    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var service: UserService

    fun ok(block: UserRes.() -> Unit) = ResponseEntity(UserRes().apply(block), HttpStatus.OK)

    fun bad(block: UserRes.() -> Unit) = ResponseEntity(UserRes().apply(block), HttpStatus.BAD_REQUEST)

    @GetMapping(path = ["all"], produces = ["application/json"])
    fun getAll() = ok {
            users = service.getAll()
        }

    @GetMapping(path = ["{id}"], produces = ["application/json"])
    fun getUser(@PathVariable id: Long) =
        ok {
            users = listOf(service[id])
        }

    @GetMapping(path=["spec"], produces = ["application/json"])
    fun getSpecificUsers(@RequestParam ids: String) =
        ok {
            users = ids.split(";")
                .map {
                    try {
                        service[it.toLong()]
                    } catch (e: Exception) {
                        null
                    }
                }.filterNotNull()
        }

    @PostMapping(path = ["login"], consumes = ["application/json"], produces = ["application/json"])
    fun login(@Valid @RequestBody req: UserReq) =
        ok {
            service loadUserByUsername req.email
            auth.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
            token = this@UserController.jwt.generate(req.email, listOf("USER"))
        }

    @PutMapping(path = ["register"], consumes = ["application/json"], produces = ["application/json"])
    fun register(@Valid @RequestBody req: UserReq) =
        try {
            service loadUserByUsername req.email
            ResponseEntity(UserRes(msg = "User ${req.email} already exist"), HttpStatus.CONFLICT)
        } catch (e: EmptyResultDataAccessException) {
            service.add(req.email, req.password, req.name)
            auth.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
            ResponseEntity(UserRes(token = jwt.generate(req.email, arrayListOf("USER"))), HttpStatus.CREATED)
        }

    @DeleteMapping(path = ["delete"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@Valid @RequestBody req: UserReq, raw: HttpServletRequest) =
        ok {
            val email = service.loadUserByUsername(jwt decode raw).email
            auth.authenticate(UsernamePasswordAuthenticationToken(email, req.password))
            service delete req.email
            msg = "Successfully delete account"
        }

    @ExceptionHandler(Exception::class)
    fun handleErrors(req: HttpServletRequest, e: Exception) =
        bad {
            msg = when (e) {
                is AuthenticationException -> "Password incorrect"
                is EmptyResultDataAccessException -> "User didn't exist. Check email and password"
                is IllegalArgumentException -> "You should specify token for this operation"
                else -> """
                    Unexpected exception, try later or contact support with this message
                    $e
                    $req
                    """.trimIndent()
            }
        }
}