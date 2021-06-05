package lab3.utils

import com.rabbitmq.client.ConnectionFactory
import mu.KotlinLogging
import org.apache.commons.lang3.SerializationUtils
import java.io.Serializable

object MQSender {

    operator fun invoke(queueName: String, msg: Serializable) {
        ConnectionFactory().newConnection().use {
            with(it.createChannel()) { basicPublish("", queueName, null, SerializationUtils.serialize(msg)) }
        }
        KotlinLogging.logger {}.debug { "[x] Request $msg to $queueName send" }
    }
}