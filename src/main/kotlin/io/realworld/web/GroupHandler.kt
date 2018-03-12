package io.realworld.web

import io.realworld.exception.InvalidRequest
import io.realworld.jwt.ApiKeySecured
import io.realworld.model.Group
import io.realworld.repository.GroupRepository
import io.realworld.repository.UserRepository
import io.realworld.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class GroupHandler(val repository: GroupRepository,
                   val userRepository: UserRepository,
                   val userService: UserService) {

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/groups")
    fun groups(): Any {
        return userRepository.findByUsername(userService.currentUser.get().username)!!.groups
    }

    @ApiKeySecured
    @PostMapping("/api/groups")
    fun newGroup(@Valid @RequestBody group: Group, errors: Errors): Any {
        InvalidRequest.check(errors)
        val user = userService.currentUser()

        val groups = repository.findByName(group.name)
        val newGroup = if (groups.isEmpty())
            Group(group.name, listOf(userRepository.findByUsername(user.username)!!))
        else {
            groups[0].users += user
            groups[0]
        }

        repository.save(newGroup)
        return newGroup
    }

    @ApiKeySecured
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/api/groups/{id}")
    fun deleteGroup(@PathVariable id: Long): Any {
        val group = repository.findById(id)
        repository.removeById(id)
        return group
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/groups/{partialName}")
    fun findByPartialName(@PathVariable partialName: String): Any {
        return repository.findByPartialName(partialName)
    }
}