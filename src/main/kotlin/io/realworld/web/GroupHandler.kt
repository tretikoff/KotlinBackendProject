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
import java.util.logging.Level
import java.util.logging.Logger
import javax.validation.Valid

@RestController
class GroupHandler(val repository: GroupRepository,
                   val userRepository: UserRepository,
                   val userService: UserService) {

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/groups")
    fun groups(): Any {
        val user = userRepository.findByUsername(userService.currentUser.get().username)
        var groups = user!!.groups;
        for (group in groups)
            Logger.getAnonymousLogger().log(Level.SEVERE, group.toString())
        return groupsView(groups)
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
        Logger.getAnonymousLogger().log(Level.SEVERE, newGroup.toString())
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
        val foundGroups = repository.findByPartialName(partialName)
        Logger.getAnonymousLogger().log(Level.SEVERE, foundGroups.toString())
        return groupsView(foundGroups)
    }

    fun groupsView(groups: List<Group>) = mapOf("groups" to groups)
}