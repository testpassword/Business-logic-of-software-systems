package lab2.services

import lab2.models.Advert
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service class ModeratorService {

    infix fun moderate(a: Advert) = Random.nextInt(0, 10) < 8
}