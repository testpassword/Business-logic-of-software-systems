package lab3.dtos.message

import lab3.utils.Statistic
import java.io.Serializable
import java.util.*

open class Req(val date: Long = Date().time): Serializable

data class StatisticReq(val action: Statistic.ACTIONS): Req()