package io.realworld.model

import com.fasterxml.jackson.annotation.JsonRootName
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.sql.Date
import javax.persistence.*

@Entity
@JsonRootName("event")
data class Event(var title: String = "",
                 var start: Date,
                 var end: Date,
                 @ManyToOne(fetch = FetchType.LAZY)
                 @Cascade(CascadeType.ALL)
                 @JoinColumn(name = "user_id")
                 var user: User = User(),
                 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                 var id: Long = 0)