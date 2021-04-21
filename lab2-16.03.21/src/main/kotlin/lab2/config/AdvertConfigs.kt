package lab2.config

import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter


@EnableJpaRepositories(basePackages = ["lab2.repos"], entityManagerFactoryRef = "advertEntityManager",
    transactionManagerRef = "transactionManager")
class AdvertConfig {

    @Bean(initMethod = "init", destroyMethod = "close") fun advertDatasource() = AtomikosDataSourceBean()

    @Bean fun advertEntityManager() =
        LocalContainerEntityManagerFactoryBean().apply {
            dataSource = advertDatasource()
            jpaVendorAdapter = HibernateJpaVendorAdapter()
            setPackagesToScan("lab2.repos")
        }
}