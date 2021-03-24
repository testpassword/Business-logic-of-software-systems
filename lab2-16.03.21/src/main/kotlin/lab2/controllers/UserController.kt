package lab2.controllers

import lab2.dtos.UserReq
import lab2.dtos.UserRes
import lab2.dtos.bad
import lab2.security.JWTTokenUtil
import lab2.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController @RequestMapping(path = ["/user"])
class UserController {

    @Autowired private lateinit var auth: AuthenticationManager
    @Autowired private lateinit var token: JWTTokenUtil
    @Autowired private lateinit var service: UserService

    @GetMapping(path = ["all"], produces = ["application/json"])
    fun getAll() = ok(UserRes(users = service.getAll()))

    @GetMapping(path = ["{id}"], produces = ["application/json"])
    fun getUser(@PathVariable id: Long) =
        try {
            ok(UserRes(users = listOf(service[id])))
        } catch (e: EmptyResultDataAccessException) {
            bad(UserRes(msg = "User with this id didn't exist"))
        }

    @PostMapping(path = ["login"], consumes = ["application/json"], produces = ["application/json"])
    fun login(@Valid @RequestBody req: UserReq) =
        try {
            (service loadUserByUsername req.email).let {
                auth.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
                ok(UserRes(token = token.generate(it.email, listOf("USER"))))
            }
        } catch (e: Exception) {
            bad(UserRes(msg = when(e) {
                is EmptyResultDataAccessException -> "User didn't exist. Check email and password"
                is AuthenticationException -> "Password incorrect"
                else -> e.toString()
            }))
        }

    @PutMapping(path = ["register"], consumes = ["application/json"], produces = ["application/json"])
    fun register(@Valid @RequestBody req: UserReq) =
        try {
            service loadUserByUsername req.email
            ResponseEntity(UserRes(msg = "User ${req.email} already exist"), HttpStatus.CONFLICT)
        } catch (e: EmptyResultDataAccessException) {
            service.add(req.email, req.password, req.username)
            auth.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
            ResponseEntity(UserRes(token = token.generate(req.email, arrayListOf("USER"))), HttpStatus.CREATED)
        }

    @PatchMapping(path = ["modify"], consumes = ["application/json"], produces = ["application/json"])
    fun modify(@Valid @RequestBody req: UserReq, raw: HttpServletRequest) =
        try {
            (service loadUserByUsername req.email).let {
                if (req.email.isNotBlank() && service.exist(req.email)) ResponseEntity(UserRes(msg = "Mail is busy"), HttpStatus.CONFLICT)
                else {
                    if (req.username.isNotBlank()) it.name = req.username
                    if (req.password.isNotBlank()) it.password = req.password
                    service.save(it)
                    ResponseEntity(UserRes(token = token.generate(req.email, listOf("USER"))), HttpStatus.ACCEPTED)
                }
            }
        } catch (e: EmptyResultDataAccessException) {
            bad(UserRes(msg = "User didn't exist"))
        }

    @DeleteMapping(path = ["delete"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@Valid @RequestBody req: UserReq, raw: HttpServletRequest) =
        try {
            auth.authenticate(UsernamePasswordAuthenticationToken(req.email, req.password))
            service delete req.email
        } catch (e: AuthenticationException) {
            ResponseEntity(UserRes("Bad password"), HttpStatus.UNAUTHORIZED)
        }
}