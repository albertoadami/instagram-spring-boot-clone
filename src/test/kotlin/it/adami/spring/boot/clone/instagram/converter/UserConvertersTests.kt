package it.adami.spring.boot.clone.instagram.converter

import it.adami.spring.boot.clone.instagram.command.CreateUserCommand
import it.adami.spring.boot.clone.instagram.dto.request.CreateUserDto
import it.adami.spring.boot.clone.instagram.entity.UserEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockedStatic
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals

class UserConvertersTests {

    private lateinit var mockedUUID: MockedStatic<UUID>
    private lateinit var mockedInstant: MockedStatic<java.time.Instant>
    private val fixedUUID = UUID.fromString("12345678-1234-5678-1234-567812345678")
    private val fixedInstant = Instant.parse("2023-10-01T12:00:00Z")

    @BeforeEach
    fun setup() {
        mockedUUID = org.mockito.Mockito.mockStatic(UUID::class.java)
        mockedInstant = org.mockito.Mockito.mockStatic(java.time.Instant::class.java)
        mockedInstant.`when`<Instant> { Instant.now() }.thenReturn(fixedInstant)
        mockedUUID.`when`<UUID> { UUID.randomUUID() }.thenReturn(fixedUUID)
    }

    @AfterEach
    fun tearDown() {
        mockedUUID.close()
        mockedInstant.close()
    }

    @Test
    fun testUserDtoToCommand() {
        val createUserDto = CreateUserDto(
            name = "Test",
            surname = "User",
            username = "testuser",
            email = "test@test.it",
            password = "password123",
            dateOfBirth = java.time.LocalDate.of(1990, 1, 1)
        )
        val expected = CreateUserCommand(
            name = createUserDto.name,
            surname = createUserDto.surname,
            username = createUserDto.username,
            email = createUserDto.email,
            password = createUserDto.password,
            dateOfBirth = createUserDto.dateOfBirth
        )

        assertEquals(expected, createUserDto.toCommand())
    }

    @Test
    fun testUserCommandToEntity() {
        val createUserCommand = CreateUserCommand(
            name = "Test",
            surname = "User",
            username = "testuser",
            email = "test@test.it",
            password = "password123",
            dateOfBirth = java.time.LocalDate.of(1990, 1, 1)
        )

        val expected = UserEntity(
            id = fixedUUID,
            name = createUserCommand.name,
            surname = createUserCommand.surname,
            username = createUserCommand.username,
            email = createUserCommand.email,
            password = createUserCommand.password,
            dateOfBirth = createUserCommand.dateOfBirth,
            createdAt = fixedInstant
        )

        val result = createUserCommand.toEntity()

        assertEquals(expected, result)
    }

}