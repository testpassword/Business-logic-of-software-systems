package lab2

import lab2.security.JWTTokenUtil
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component class CorsFilter: Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) =
        chain.doFilter(request, (response as HttpServletResponse).apply {
            setHeader("Access-Control-Allow-Origin", "*")
            setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH")
            setHeader("Access-Control-Allow-Headers", "x-requested-with")
        })
}

class JWTFilter(val jwtToken: JWTTokenUtil): GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        jwtToken.resolve(request as HttpServletRequest).also {
            if (jwtToken.validate(it)) SecurityContextHolder.getContext().authentication = jwtToken.getAuthentication(it)
        }
        chain.doFilter(request, response)
    }
}