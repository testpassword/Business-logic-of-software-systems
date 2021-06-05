package lab3.config

import lab3.utils.ARCH_QUEUE_NAME
import lab3.utils.STAT_QUEUE_NAME
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component class RMQConfig {

    @Autowired private lateinit var admin: AmqpAdmin

    @PostConstruct fun initQueues() =
        arrayOf(ARCH_QUEUE_NAME, STAT_QUEUE_NAME).forEach { admin.declareQueue(Queue(it, false)) }
}