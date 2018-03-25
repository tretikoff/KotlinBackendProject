package io.realworld.web

import io.realworld.exception.NotFoundException
import io.realworld.jwt.ApiKeySecured
import io.realworld.model.User
import io.realworld.model.inout.Profile
import io.realworld.repository.UserRepository
import io.realworld.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class ProfileHandler(val userRepository: UserRepository,
                     val userService: UserService) {

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/profiles/{username}")
    fun profile(@PathVariable username: String): Any {
        userRepository.findByUsername(username)?.let {
            return view(it, userService.currentUser())
        }
        throw NotFoundException()
    }

    fun view(user: User, currentUser: User) = mapOf("profile" to Profile.fromUser(user, currentUser))

}
