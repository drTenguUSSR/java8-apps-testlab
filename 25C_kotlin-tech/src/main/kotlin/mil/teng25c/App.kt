package mil.teng25c

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableConfigurationProperties
@EnableTransactionManagement
class App

fun main(args: Array<String>) {
 SpringApplication.run(App::class.java, *args)
}