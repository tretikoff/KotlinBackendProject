package io.realworld.repository

import io.realworld.model.Group
import io.realworld.model.News
import io.realworld.model.Note
import io.realworld.model.User
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface NewsRepository : PagingAndSortingRepository<News, Long> {
    fun findByTitle(title: String): List<News>
    @Query("SELECT g " +
            "FROM News g " +
            "WHERE g.group = :group")
    fun findAllByGroup(@Param("group") group: Group): List<News>
}