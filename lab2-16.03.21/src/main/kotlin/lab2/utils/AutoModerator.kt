package lab2.utils

import lab2.models.Advert
import org.springframework.core.io.ClassPathResource

object AutoModerator {

    val BAD_WORDS = ClassPathResource("bad_words.txt").file.readLines().toSet()

    operator fun invoke(a: Advert) =
        listOf(a.location, a.description, a.name, a.mobileNumber, a.image)
            .flatMap { it.split(" ") }
            .toSet()
            .intersect(BAD_WORDS)
}