package lab3.utils

import com.beust.klaxon.Klaxon
import lab3.dtos.message.StatisticReq
import lab3.models.Advert
import lab3.services.AdvertService
import lab3.services.UserService
import mu.KotlinLogging
import org.apache.commons.lang3.SerializationUtils
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

const val STAT_QUEUE_NAME = "STATISTIC"

@Component @EnableScheduling @RabbitListener(queues = [STAT_QUEUE_NAME])
class Statistic {

    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var advertService: AdvertService
    @Autowired private lateinit var postman: Postman
    var cached: Map<String, Any> = emptyMap()

    enum class ACTIONS { RECOMPUTE }

    @PostConstruct fun getStatisticOnStartup() { cached = compute() }

    // every midnight
    @Scheduled(cron = "0 0 0 * * *") fun sendComputeTaskReq(recipient: String) =
        MQSender(STAT_QUEUE_NAME, StatisticReq(ACTIONS.RECOMPUTE, recipient))

    @RabbitHandler fun getTaskReq(res: ByteArray) =
        with(SerializationUtils.deserialize<StatisticReq>(res)) {
            when (action) {
                ACTIONS.RECOMPUTE -> postman(recipient, "Actual statistic", Klaxon().toJsonString(compute()))
            }
        }

    private fun compute(): Map<String, Any> {
        val stat = mapOf(
            "users" to userService.getAll().let { u ->
                mapOf(
                    "total" to u.count(),
                    "describers" to u.count { it.isDescriber }
                )},
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
                )}
        )
        KotlinLogging.logger {}.debug { "Statistic recomputed" }
        return stat
    }
}