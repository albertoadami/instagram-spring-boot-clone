package it.adami.spring.boot.clone.instagram.exception

class UsernameAlreadyInUseException(val userName: String) :
    RuntimeException("Username $userName is already in use")

class EmailAlreadyInUseException(val email: String) :
    RuntimeException("Email $email is already in use")