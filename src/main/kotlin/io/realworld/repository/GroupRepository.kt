package io.realworld.repository

import io.realworld.model.Group
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface GroupRepository : PagingAndSortingRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    @Transactional
    fun removeById(id: Long)
    fun findByName(name: String): List<Group>

    @Query("SELECT g " +
            "FROM Group g " +
            "WHERE g.name LIKE CONCAT('%', :partialName, '%')")
    fun findByPartialName(@Param("partialName") partialName: String): List<Group>
}