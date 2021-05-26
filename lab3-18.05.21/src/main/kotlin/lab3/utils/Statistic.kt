package lab3.utils

import lab3.MIDNIGHT
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@EnableScheduling object Statistic {

    var cached: Map<String, String> = emptyMap()
    val QUEUE_NAME = "STAT"
    val UPD_REQ = "UPDATE_STAT"

    fun updateReq() = MQSender(QUEUE_NAME, UPD_REQ)

    @Scheduled(cron = MIDNIGHT) fun compute() {
    }
}