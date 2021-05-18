package lab3

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication class Lab2Application
fun main(args: Array<String>) {
    with(SpringApplication(Lab2Application::class.java)) {
        setBannerMode(Banner.Mode.OFF)
        run(*args)
    }
}