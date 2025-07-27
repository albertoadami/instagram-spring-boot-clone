package it.adami.spring.boot.clone.instagram.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/health")
    fun get(): ResponseEntity<Void> {
        return ResponseEntity.noContent().build()
    }
}