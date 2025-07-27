package it.adami.spring.boot.clone.instagram.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "users")
data class UserEntity(
                 @Id
                 val id: UUID,
                 val name: String,
                 val surname: String,
                 val email: String,
                 val password: String,
                 val username: String,
                 val dateOfBirth: LocalDate,
                 val createdAt: Instant)
