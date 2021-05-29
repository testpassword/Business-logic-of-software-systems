package lab3.utils

import com.rabbitmq.client.ConnectionFactory
import mu.KotlinLogging
import java.nio.charset.StandardCharsets

object MQSender {

    operator fun invoke(queueName: String, msg: String) =
        KotlinLogging.logger {}.debug {
            ConnectionFactory().newConnection().use {
                with(it.createChannel()) {
                    queueDeclare(STAT_QUEUE_NAME, false, false, false, null)
                    basicPublish("", queueName, null, msg.toByteArray(StandardCharsets.UTF_8))
                }
        }
            " [x] Request $msg to $queueName send"
    }
}