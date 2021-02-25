package testpassword.lab1;

import lombok.val;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication public class Lab1Application {
    public static void main(String[] args) {
        val app = new SpringApplication(Lab1Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}