package lab3.utils

import lab3.models.Advert
import lab3.services.AdvertService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.*
import java.util.*
import javax.imageio.ImageIO

const val ARCH_QUEUE_NAME = "ARCHIVER"
const val ARCH_COMPRESS_REQ = "ARCHIVER_COMPRESS"

@EnableScheduling @Component object Archiver {

    @Autowired private lateinit var advertService: AdvertService

    // once in month
    @Scheduled(cron = "* 0 0 1 * *") fun archiveTaskReq() = MQSender(ARCH_QUEUE_NAME, ARCH_COMPRESS_REQ)

    private fun archive() {
        advertService.getAll().filter { it.status == Advert.STATUS.CLOSED && !it.archived }.forEach {
            it.image = it.image.base64toBytes().compressImage().bytesToBase64()
            it.archived = true
            advertService.add(it)
        }
    }

    private fun String.base64toBytes() = Base64.getDecoder().decode(this)

    private fun ByteArray.bytesToBase64() = Base64.getEncoder().encodeToString(this)

    private fun ByteArray.compressImage() =
        ByteArrayOutputStream(this.size).use {
            ImageIO.write(ImageIO.read(ByteArrayInputStream(this)), "jpg", it)
            return@use it.toByteArray()
        }
}

@Component class ArchiverReceiver {

    @JmsListener(destination = ARCH_QUEUE_NAME) fun receive(msg: String) = println(msg)
}