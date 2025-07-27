package it.adami.spring.boot.clone.instagram.controller

import com.fasterxml.jackson.databind.ObjectMapper
import it.adami.spring.boot.clone.instagram.converter.toCommand
import it.adami.spring.boot.clone.instagram.domain.UserId
import it.adami.spring.boot.clone.instagram.dto.request.CreateUserDto
import it.adami.spring.boot.clone.instagram.exception.EmailAlreadyInUseException
import it.adami.spring.boot.clone.instagram.exception.UsernameAlreadyInUseException
import it.adami.spring.boot.clone.instagram.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.util.*

@WebMvcTest(UserController::class)
@ActiveProfiles("test")
class UserControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var userService: UserService

    private lateinit var createUserDto: CreateUserDto

    @BeforeEach
    fun setUp() {
        createUserDto = CreateUserDto(
            name = "Test",
            surname = "User",
            username = "testuser",
            email = "test@test.it",
            password = "password123",
            dateOfBirth = LocalDate.of(1990, 1, 1)
        )
    }

    @Test
    fun createUserSuccessfully() {
        // Given
        val id = UserId(UUID.randomUUID())

        // prepare stubs
        `when`(userService.createUser(createUserDto.toCommand())).thenReturn(id)

        // When/Then

        mockMvc.post("/signup") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createUserDto)
        }.andExpect {
            status { isOk() }
        }

        // Verify
        verify(userService, times(1)).createUser(createUserDto.toCommand())
    }

    @Test
    fun createUserConflictWithExistingEmail() {
        // prepare stubs
        `when`(userService.createUser(createUserDto.toCommand())).thenThrow(
            EmailAlreadyInUseException(createUserDto.email)
        )

        // When/Then
        mockMvc.post("/signup") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createUserDto)
        }.andExpect {
            status { isConflict() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.error") { value("Email already in use") }
            }
        }

        // Verify
        verify(userService, times(1)).createUser(createUserDto.toCommand())
    }

    @Test
    fun createUserConflictWithExistingUsername() {
        // prepare stubs
        `when`(userService.createUser(createUserDto.toCommand())).thenThrow(
            UsernameAlreadyInUseException(createUserDto.username)
        )
        // When/Then
        mockMvc.post("/signup") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createUserDto)
        }.andExpect {
            status { isConflict() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.error") { value("Username already in use") }
            }
        }
    }
}