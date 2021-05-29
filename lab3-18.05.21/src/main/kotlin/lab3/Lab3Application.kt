package lab3

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jms.annotation.EnableJms

@SpringBootApplication @EnableJms
class Lab3Application
fun main(args: Array<String>) {
    with(SpringApplication(Lab3Application::class.java)) {
        setBannerMode(Banner.Mode.OFF)
        run(*args)
    }
}