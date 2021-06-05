package lab3.controllers

import com.beust.klaxon.Klaxon
import lab3.dtos.http.AdminReq
import lab3.dtos.http.Res
import lab3.models.RoleException
import lab3.models.User
import lab3.security.JWTTokenUtil
import lab3.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import lab3.utils.Statistic

@RestController @RequestMapping(path = ["/admin"])
class AdminController {

    @Autowired private lateinit var jwt: JWTTokenUtil
    @Autowired private lateinit var service: UserService
    @Autowired private lateinit var statistic: Statistic

    private fun ok(raw: HttpServletRequest, block: Res.() -> Unit): ResponseEntity<Res> {
        if ((service loadUserByUsername (jwt decode raw)).role != User.ROLE.ADMIN) throw RoleException()
        else return ResponseEntity(Res().apply(block), HttpStatus.OK)
    }

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

    @GetMapping(path = ["stat"], produces = ["application/json"])
    fun getStatistic(@RequestParam isCached: Boolean = true, raw: HttpServletRequest) =
        ok(raw) {
            msg = if (isCached) Klaxon().toJsonString(statistic.cached) else {
                statistic.sendComputeTaskReq((service loadUserByUsername (jwt decode raw)).email)
                "Computing statistic started, you will get results on your email as soon as possible"
            }
        }
}