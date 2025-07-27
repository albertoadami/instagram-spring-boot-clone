package it.adami.spring.boot.clone.instagram.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(HealthController::class)
@ActiveProfiles("test")
class HealthControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldReturnNoContent() {
        mockMvc.get("/health")
            .andExpect {
                status { isNoContent() }
            }
    }

}