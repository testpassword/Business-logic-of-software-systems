package testpassword.lab1.services;

import org.springframework.stereotype.Service;
import testpassword.lab1.models.Advert;
import java.util.Random;

@Service public class ModeratorService {

    //просто заглушка, здесь может быть любая логика
    static public boolean moderate(Advert a) { return new Random().nextInt(10 - 1) + 1 < 8; }
}