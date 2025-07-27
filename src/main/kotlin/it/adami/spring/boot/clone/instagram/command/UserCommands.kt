package it.adami.spring.boot.clone.instagram.command

import java.time.LocalDate

interface UserCommand

data class CreateUserCommand(
        val name: String,
        val surname: String,
        val username: String,
        val email: String,
        val password: String,
        val dateOfBirth: LocalDate
    ) : UserCommand
