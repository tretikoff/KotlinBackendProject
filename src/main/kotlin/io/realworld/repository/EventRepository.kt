package io.realworld.repository

import feign.Param
import io.realworld.model.Event
import io.realworld.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface EventRepository : PagingAndSortingRepository<Event, Long> {
//    fun findAllByUser(userId: User): List<Event>
    fun findByTitle(title: String): List<Event>
    @Transactional
    fun removeById(id: Long)
    @Query("SELECT g " +
            "FROM Event g " +
            "WHERE g.user = :user")
    fun findAllByUser(@Param("user") user: User): List<Event>
}