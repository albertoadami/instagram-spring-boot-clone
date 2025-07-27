package it.adami.spring.boot.clone.instagram.converter

import it.adami.spring.boot.clone.instagram.command.CreateUserCommand
import it.adami.spring.boot.clone.instagram.dto.request.CreateUserDto
import it.adami.spring.boot.clone.instagram.entity.UserEntity

fun CreateUserDto.toCommand(): CreateUserCommand {
    return CreateUserCommand(
        name = this.name,
        surname = this.surname,
        username = this.username,
        email = this.email,
        password = this.password,
        dateOfBirth = this.dateOfBirth
    )
}

fun CreateUserCommand.toEntity(): UserEntity {
    return UserEntity(
        id = java.util.UUID.randomUUID(),
        name = this.name,
        surname = this.surname,
        username = this.username,
        email = this.email,
        password = this.password,
        dateOfBirth = this.dateOfBirth,
        createdAt = java.time.Instant.now()
    )
}