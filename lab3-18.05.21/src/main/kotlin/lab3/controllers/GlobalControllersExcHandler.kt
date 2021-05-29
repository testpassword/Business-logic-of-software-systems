package lab3.controllers

import io.jsonwebtoken.JwtException
import lab3.dtos.http.Res
import lab3.models.RoleException
import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.zip.DataFormatException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice class GlobalControllersExcHandler {

    private fun bad(block: Res.() -> Unit) = ResponseEntity(Res().apply(block), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class) fun handleErrors(req: HttpServletRequest, e: Exception) =
        bad {
            KotlinLogging.logger {}.error { e.stackTraceToString() }
            msg = when (e) {
                is LockedException -> "Your account locked. Maybe you forgot to change temp password or was banned"
                is DataFormatException -> "You must specify why the ad wasn't approved"
                is AuthenticationException -> "Password incorrect"
                is MethodArgumentTypeMismatchException -> "This request required arguments"
                is RoleException -> "You doesn't have rights for this operation"
                is IllegalArgumentException -> "You should add param for change"
                is JwtException -> "Bad token or didn't presented"
                is EmptyResultDataAccessException -> "Entity didn't exist"
                else -> """
                    Unexpected exception, try later or contact support with this message
                    ${e.localizedMessage}
                    $req
                    """.trimIndent()
            }
        }
}