package io.realworld.model

import com.fasterxml.jackson.annotation.JsonRootName
import java.sql.Date
import javax.persistence.*

@Entity
@JsonRootName("event")
data class Event(var title: String = "",
                 var start: Date,
                 var end: Date,
                 @ManyToOne(cascade = arrayOf(javax.persistence.CascadeType.MERGE,
                         javax.persistence.CascadeType.PERSIST,
                         javax.persistence.CascadeType.REFRESH))
                 @JoinColumn(name = "user_id")
                 var user: User = User(),
                 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                 var id: Long = 0)