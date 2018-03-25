package io.realworld.model

import com.fasterxml.jackson.annotation.JsonRootName
import javax.persistence.*

@Entity
@JsonRootName("note")
data class Note(var title: String = "",
                var body: String = "",
                @ManyToOne(cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH))
                @JoinColumn(name="user_id" )
                   var user: User = User(),
                @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
                   var id: Long = 0)