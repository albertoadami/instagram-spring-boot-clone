package it.adami.spring.boot.clone.instagram.repository

import it.adami.spring.boot.clone.instagram.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByUsername(userName: String): UserEntity?

    fun findByEmail(email: String): UserEntity?
}