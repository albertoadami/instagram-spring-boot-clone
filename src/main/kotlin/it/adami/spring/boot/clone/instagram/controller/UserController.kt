package it.adami.spring.boot.clone.instagram.controller

import it.adami.spring.boot.clone.instagram.converter.*
import it.adami.spring.boot.clone.instagram.dto.request.CreateUserDto
import it.adami.spring.boot.clone.instagram.dto.response.GenericErrorResponse
import it.adami.spring.boot.clone.instagram.exception.EmailAlreadyInUseException
import it.adami.spring.boot.clone.instagram.exception.UsernameAlreadyInUseException
import it.adami.spring.boot.clone.instagram.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/signup")
    fun signUp(@RequestBody request: CreateUserDto): ResponseEntity<Any> {

        val cmd = request.toCommand()
        try {
            userService.createUser(cmd)
            return ResponseEntity.ok().build()
        } catch (e: EmailAlreadyInUseException) {
            return ResponseEntity.status(409).body(
                GenericErrorResponse(
                    "Email already in use"
                )
            )
        } catch (e: UsernameAlreadyInUseException) {
            return ResponseEntity.status(409).body(
                GenericErrorResponse(
                    "Username already in use"
                )
            )
        }

    }
}