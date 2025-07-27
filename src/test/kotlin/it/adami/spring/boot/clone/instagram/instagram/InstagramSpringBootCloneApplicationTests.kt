package it.adami.spring.boot.clone.instagram.instagram

import it.adami.spring.boot.clone.instagram.PostgresContainerBaseTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

// This is a simple test to check if the Spring Boot application context loads successfully.
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class InstagramSpringBootCloneApplicationTests : PostgresContainerBaseTest() {

	@Test
	fun contextLoads() {
	}

}
