package lab3.utils

import lab3.models.Advert
import lab3.services.AdvertService
import lab3.services.UserService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

const val STAT_QUEUE_NAME = "STATISTIC"
const val STAT_COMPUTE_REQ = "STATISTIC_COMPUTE"

@EnableScheduling @Component object Statistic {

    var cached: Map<String, Any> = compute()
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var advertService: AdvertService

    // every midnight
    @Scheduled(cron = "0 0 0 * * *") fun computeTaskReq() = MQSender(STAT_QUEUE_NAME, STAT_COMPUTE_REQ)

    private fun compute(): Map<String, Any> {
        val stat = mapOf(
            "users" to userService.getAll().let { u ->
                mapOf(
                    "total" to u.count(),
                    "describers" to u.count { it.isDescriber }
                ) },
            "adverts" to advertService.getAll().let { a ->
                mapOf(
                    "total" to a.count(),
                    "location" to a.groupBy(Advert::location).count(),
                    "floor" to a.groupBy(Advert::floor).count(),
                    "rooms" to a.groupBy(Advert::quantityOfRooms).count(),
                    "type" to mapOf(
                        "advert_type" to a.groupBy(Advert::typeOfAdvert).count(),
                        "estate_type" to a.groupBy(Advert::typeOfEstate).count()
                    ),
                    "images" to a.map(Advert::image).toSet().count()
                )
            }
        )
        KotlinLogging.logger {}.info { "Statistic recomputed" }
        return stat
    }
}


@Component class StatisticMQReceiver {

    @JmsListener(destination = STAT_QUEUE_NAME) fun receive(msg: String) = println(msg)
}