package lab2

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication class Lab2Application
fun main(args: Array<String>) {
    SpringApplication(Lab2Application::class.java).apply {
        setBannerMode(Banner.Mode.OFF)
        run(*args)
    }
}