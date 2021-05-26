package lab3.utils

import com.rabbitmq.client.ConnectionFactory
import mu.KotlinLogging
import java.nio.charset.StandardCharsets

object MQSender {

    private val log = KotlinLogging.logger {}

    operator fun invoke(queueName: String, msg: String) =
        ConnectionFactory().newConnection().use {
            with(it.createChannel()) {
                queueDeclare(Statistic.QUEUE_NAME, false, false, false, null)
                basicPublish("", queueName, null, msg.toByteArray(StandardCharsets.UTF_8))
                log.debug { " [x] Request $msg to $queueName send" }
            }
    }
}