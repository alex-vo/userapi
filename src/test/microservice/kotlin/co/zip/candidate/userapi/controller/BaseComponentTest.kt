package co.zip.candidate.userapi.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

internal class ComponentTestInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val dbContainer = PostgreSQLContainer("postgres")
            .withDatabaseName("user-db")
            .withUsername("postgres")
            .withPassword("YourStrong@Passw0rd")
            .withExposedPorts(5432)
            .withLogConsumer(Slf4jLogConsumer(log))

        dbContainer.waitingFor(
            Wait.forListeningPort()
                .withStartupTimeout(Duration.ofSeconds(20))
        )
        dbContainer.start()
        TestPropertyValues.of(
            "spring.datasource.url=" + dbContainer.jdbcUrl,
            "spring.datasource.username=" + dbContainer.username,
            "spring.datasource.password=" + dbContainer.password
        ).applyTo(applicationContext.environment)
    }
}

@SpringBootTest
@ContextConfiguration(initializers = [ComponentTestInitializer::class])
@AutoConfigureMockMvc
@SqlGroup(
    Sql(scripts = ["classpath:db/initTestData.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    Sql(scripts = ["classpath:db/clearTestData.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
)
abstract class BaseComponentTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    val jacksonObjectMapper = jacksonObjectMapper()

    final inline fun <reified R> MvcResult.getResponseDTO(): R {
        return this.response.contentAsString
            .asDTO()
    }

    final inline fun <reified R> String.asDTO(): R {
        return jacksonObjectMapper.readValue(this, object : TypeReference<R>() {})
    }

//    lateinit var precreatedValidUser: UserEntity
//    lateinit var precreatedUserLowSalaryExpenseRatio: UserEntity
//    lateinit var precreatedValidUserAccount: AccountEntity

    final inline fun <reified T> performSuccessfulPost(url: String, body: String): T {
        return mockMvc.post(url) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }
            .andExpect { status { isOk() } }
            .andReturn()
            .response
            .contentAsString
            .let {
                jacksonObjectMapper.readValue(it, T::class.java)
            }
    }

    final inline fun <reified T> performSuccessfulGet(url: String): T {
        return mockMvc.get(url)
            .andExpect { status { isOk() } }
            .andReturn()
            .response
            .contentAsString
            .let {
                jacksonObjectMapper.readValue(it, T::class.java)
            }
    }

}

fun String.asResourceContent(): String {
    return File(BaseComponentTest::class.java.getResource(this).path).readText()
}

