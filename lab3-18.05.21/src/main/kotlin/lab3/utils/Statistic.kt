package lab3.utils

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

const val STAT_QUEUE_NAME = "STATISTIC"

@Component @EnableScheduling @RabbitListener(queues = [STAT_QUEUE_NAME])
class Statistic {

    enum class ACTIONS { RECOMPUTE }

    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var advertService: AdvertService
    val cached: Map<String, Any>? = null

    // every midnight
    @Scheduled(cron = "0 0 0 * * *") fun sendComputeTaskReq() = MQSender(STAT_QUEUE_NAME, StatisticReq(ACTIONS.RECOMPUTE))

    @RabbitHandler fun getComputeTaskReq(res: ByteArray) =
        when (SerializationUtils.deserialize<StatisticReq>(res).action) {
            ACTIONS.RECOMPUTE -> println("its working!")
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