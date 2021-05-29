package lab3.utils

import com.rabbitmq.client.ConnectionFactory
import lab3.dtos.message.Req
import org.apache.commons.lang3.SerializationUtils

object MQSender {

    operator fun invoke(queueName: String, msg: Req) =
        ConnectionFactory().newConnection().use {
            with(it.createChannel()) {
                queueDeclare(STAT_QUEUE_NAME, false, false, false, null)
                basicPublish("", queueName, null, SerializationUtils.serialize(msg))
            }
        }
}