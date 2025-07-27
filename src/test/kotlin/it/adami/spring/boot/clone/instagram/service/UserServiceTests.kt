package it.adami.spring.boot.clone.instagram.service

import it.adami.spring.boot.clone.instagram.command.CreateUserCommand
import it.adami.spring.boot.clone.instagram.converter.toEntity
import it.adami.spring.boot.clone.instagram.domain.UserId
import it.adami.spring.boot.clone.instagram.exception.EmailAlreadyInUseException
import it.adami.spring.boot.clone.instagram.exception.UsernameAlreadyInUseException
import it.adami.spring.boot.clone.instagram.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UserServiceTests {

    private lateinit var mockedUUID: MockedStatic<UUID>
    private lateinit var mockedInstant: MockedStatic<Instant>
    private val fixedUUID = UUID.fromString("12345678-1234-5678-1234-567812345678")
    private val fixedInstant = Instant.parse("2023-10-01T12:00:00Z")

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        // Initialize the service with the mocked repository
        userService = UserServiceImpl(userRepository)

        // Setup static mocks
        mockedUUID = Mockito.mockStatic(UUID::class.java)
        mockedInstant = Mockito.mockStatic(Instant::class.java)
        mockedInstant.`when`<Instant> { Instant.now() }.thenReturn(fixedInstant)
        mockedUUID.`when`<UUID> { UUID.randomUUID() }.thenReturn(fixedUUID)
    }

    @AfterEach
    fun tearDown() {
        mockedUUID.close()
        mockedInstant.close()
    }

    @Test
    fun createUserReturnUserId() {
        // Given
        val createUserCommand = CreateUserCommand(
            name = "Test",
            surname = "User",
            username = "testuser",
            email = "test@test.it",
            password = "password123",
            dateOfBirth = LocalDate.of(1990, 1, 1)
        )
        val expectedUserId = UserId(fixedUUID) // Use the fixed UUID from mock
        val entity = createUserCommand.toEntity()

        // Mock repository behavior
        `when`(userRepository.findByEmail(createUserCommand.email)).thenReturn(null)
        `when`(userRepository.findByUsername(createUserCommand.username)).thenReturn(null)
        `when`(userRepository.save(entity)).thenReturn(entity)

        // When
        val result = userService.createUser(createUserCommand)

        // Then
        assertEquals(expectedUserId, result)

        // Verify repository interactions
        verify(userRepository).findByEmail(createUserCommand.email)
        verify(userRepository).findByUsername(createUserCommand.username)
        verify(userRepository).save(entity)
    }

    @Test
    fun createUserThrowsExceptionWhenEmailExists() {
        // Given
        val createUserCommand = CreateUserCommand(
            name = "Test",
            surname = "User",
            username = "testuser",
            email = "test@test.it",
            password = "password123",
            dateOfBirth = LocalDate.of(1990, 1, 1)
        )
        val existingEntity = createUserCommand.toEntity()

        // Mock repository to return existing user with same email
        `when`(userRepository.findByEmail(createUserCommand.email)).thenReturn(existingEntity)

        // When & Then
        val exception = org.junit.jupiter.api.assertThrows<EmailAlreadyInUseException> {
            userService.createUser(createUserCommand)
        }

        assertEquals("Email test@test.it is already in use", exception.message)

        // Verify only email check was called
        verify(userRepository).findByEmail(createUserCommand.email)
    }

    @Test
    fun createUserThrowsExceptionWhenUsernameExists() {
        // Given
        val createUserCommand = CreateUserCommand(
            name = "Test",
            surname = "User",
            username = "testuser",
            email = "test@test.it",
            password = "password123",
            dateOfBirth = LocalDate.of(1990, 1, 1)
        )
        val existingEntity = createUserCommand.toEntity()

        // Mock repository behavior
        `when`(userRepository.findByEmail(createUserCommand.email)).thenReturn(null)
        `when`(userRepository.findByUsername(createUserCommand.username)).thenReturn(existingEntity)

        // When & Then
        val exception = org.junit.jupiter.api.assertThrows<UsernameAlreadyInUseException> {
            userService.createUser(createUserCommand)
        }

        assertEquals("Username ${createUserCommand.username} is already in use", exception.message)

        // Verify both checks were called but save was not
        verify(userRepository).findByEmail(createUserCommand.email)
        verify(userRepository).findByUsername(createUserCommand.username)
        verify(userRepository, Mockito.never()).save(Mockito.any())
    }
}