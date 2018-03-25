package io.realworld.model

import com.fasterxml.jackson.annotation.JsonRootName
import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "events")
@JsonRootName("event")
data class Event(var title: String = "",
                 @Column(name="start_date")
                 var start: Date = Date(1),
                 @Column(name="end_date", nullable = true)
                 var end: Date? = null,
                 @ManyToOne(cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH))
                 @JoinColumn(name = "user_id")
                 var user: User = User(),
                 @Id @GeneratedValue(strategy = GenerationType.AUTO)
                 var id: Long = 0)