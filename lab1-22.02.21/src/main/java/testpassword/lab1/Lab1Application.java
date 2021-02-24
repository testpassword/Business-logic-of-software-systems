package testpassword.lab1;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication public class Lab1Application {
    public static void main(String[] args) {
        var app = new SpringApplication(Lab1Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}