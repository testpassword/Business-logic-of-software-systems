package lab3.utils

import lab3.dtos.message.ArchiverReq
import lab3.models.Advert
import lab3.repos.AdvertRepo
import mu.KotlinLogging
import org.apache.commons.lang3.SerializationUtils
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.*
import java.util.*
import javax.imageio.ImageIO

const val ARCH_QUEUE_NAME = "ARCHIVER"

@Component @EnableScheduling @RabbitListener(queues = [ARCH_QUEUE_NAME])
class Archiver {

    @Autowired private lateinit var advertRepo: AdvertRepo

    enum class ACTIONS { COMPRESS }

    @Profile("slave")
    @Scheduled(fixedDelay = 20_000) // every 20 sec
    // @Scheduled(cron = "* 0 0 1 * *") // once in month
    fun sendArchiveTaskReq() = MQSender(ARCH_QUEUE_NAME, ArchiverReq(ACTIONS.COMPRESS))

    @RabbitHandler fun getTaskReq(res: ByteArray) =
        when (SerializationUtils.deserialize<ArchiverReq>(res).action) { ACTIONS.COMPRESS -> archive() }

    private fun archive() {
        val candidates = advertRepo.findAll().filter { it.status == Advert.STATUS.CLOSED && !it.archived }
        candidates.forEach {
            it.image = it.image.base64toBytes().compressImage().bytesToBase64()
            it.archived = true
            advertRepo.save(it)
        }
        KotlinLogging.logger {}.debug { """
            
            Archived adverts: $candidates
            Total: ${candidates.count()}
        """.trimIndent() }
    }

    private fun String.base64toBytes() = Base64.getDecoder().decode(this)

    private fun ByteArray.bytesToBase64() = Base64.getEncoder().encodeToString(this)

    private fun ByteArray.compressImage() =
        ByteArrayOutputStream(this.size).use {
            ImageIO.write(ImageIO.read(ByteArrayInputStream(this)), "jpg", it)
            return@use it.toByteArray()
        }
}