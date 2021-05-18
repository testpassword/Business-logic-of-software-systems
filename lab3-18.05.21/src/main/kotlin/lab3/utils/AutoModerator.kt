package lab3.utils

import lab3.models.Advert
import mu.KotlinLogging
import org.springframework.core.io.ClassPathResource

object AutoModerator {

    private val log = KotlinLogging.logger {}

    val BAD_WORDS =
        try {
            ClassPathResource("bad_words.txt").file.readLines().toSet()
        } catch (e: Exception) {
            log.error { "File with bad words didn't exist, automoderation disabled" }
            emptySet()
        }

    operator fun invoke(a: Advert) =
        listOf(a.location, a.description, a.name, a.mobileNumber, a.image)
            .flatMap { it.split(" ") }
            .toSet()
            .intersect(BAD_WORDS)
}