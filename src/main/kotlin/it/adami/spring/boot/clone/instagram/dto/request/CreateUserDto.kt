package it.adami.spring.boot.clone.instagram.dto.request

import java.time.LocalDate

data class CreateUserDto(val name: String,
                         val surname: String,
                         val username: String,
                         val email: String,
                         val password: String,
                         val dateOfBirth: LocalDate)