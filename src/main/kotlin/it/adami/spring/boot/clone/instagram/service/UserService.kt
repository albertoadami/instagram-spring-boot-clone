package it.adami.spring.boot.clone.instagram.service

import it.adami.spring.boot.clone.instagram.command.CreateUserCommand
import it.adami.spring.boot.clone.instagram.converter.toEntity
import it.adami.spring.boot.clone.instagram.domain.UserId
import it.adami.spring.boot.clone.instagram.exception.EmailAlreadyInUseException
import it.adami.spring.boot.clone.instagram.exception.UsernameAlreadyInUseException
import it.adami.spring.boot.clone.instagram.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

interface UserService {

    fun createUser(cmd: CreateUserCommand): UserId

}
@Service
class UserServiceImpl(val userRepository: UserRepository) : UserService {

    @Transactional
    override fun createUser(cmd: CreateUserCommand): UserId {
        val entityByEmail = userRepository.findByEmail(cmd.email)
        val entityByUsername = userRepository.findByUsername(cmd.username)

        if(entityByEmail != null) {
            throw  EmailAlreadyInUseException(cmd.email)
        }

        if(entityByUsername != null) {
            throw  UsernameAlreadyInUseException(cmd.username)
        }

        val userEntity = cmd.toEntity()

        val result = userRepository.save(userEntity)

        return UserId(result.id)

    }


}