package io.realworld.web

import io.realworld.jwt.ApiKeySecured
import io.realworld.model.Group
import io.realworld.model.News
import io.realworld.repository.GroupRepository
import io.realworld.repository.NewsRepository
import io.realworld.repository.UserRepository
import io.realworld.service.UserService
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class NewsHandler(val repository: NewsRepository,
                  val groupRepository: GroupRepository,
                   val userRepository: UserRepository,
                   val userService: UserService) {

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/news")
    fun groupNews(@Valid @RequestBody id: Long, errors: Errors): Any {
        val currentGroup = groupRepository.findById(id)
        val groupNews = repository.findAllByGroup(currentGroup.get())

        return newsView(groupNews)
    }

    @ApiKeySecured
    @PostMapping("/api/news")
    fun newGroup(@Valid @RequestBody id: Long, newNews: News, errors: Errors): Any {

        val currentGroup = groupRepository.findById(id).get()
        newNews.group = currentGroup
        repository.save(newNews)

        return newNews
    }

    fun newsView(news: List<News>) = mapOf("news" to news)

}