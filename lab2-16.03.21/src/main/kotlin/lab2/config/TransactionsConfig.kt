package lab2.config

import com.atomikos.icatch.jta.UserTransactionManager
import lab2.services.AdvertService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.jta.JtaTransactionManager

@Configuration @EnableTransactionManagement class TransactionsConfig {

    @Bean(initMethod = "init", destroyMethod = "close") fun userTransactionManager() =
        UserTransactionManager().apply {
            setTransactionTimeout(300)
            forceShutdown = true
        }

    @Bean fun transactionManager() =
        JtaTransactionManager().apply {
            val trans = userTransactionManager()
            transactionManager = trans
            userTransaction = trans
        }

    @Bean fun createAdvertService() = AdvertService()
}