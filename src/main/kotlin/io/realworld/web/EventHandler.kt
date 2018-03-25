package io.realworld.web

import io.realworld.jwt.ApiKeySecured
import io.realworld.model.Event
import io.realworld.model.User
import io.realworld.model.inout.EventData
import io.realworld.repository.EventRepository
import io.realworld.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
class EventHandler(val repository: EventRepository,
                   val userService: UserService) {

    private val format = SimpleDateFormat("yyyy-MM-dd")

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/events")
    fun articles(): Any {
        val events = repository.findAllByUser(userService.currentUser())
        return eventsView(events, userService.currentUser())
    }

    @ApiKeySecured
    @PostMapping("/api/events")
    fun newNote(@Valid @RequestBody eventData: EventData, errors: Errors): Any {
        var event = dataToEvent(eventData)
        repository.save(event)
        val events = repository.findByTitle(event.title)
        event = events[events.size - 1]
        return eventToData(event)
    }

    @ApiKeySecured
    @PutMapping("/api/events")
    fun editNote(@Valid @RequestBody eventData: EventData, errors: Errors): Any {
        var event = dataToEvent(eventData)
        val id = eventData.id
        event.id = id

        repository.save(event)
        event = repository.findById(id).get()
        return eventToData(event)
    }

    @ApiKeySecured
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/api/events/{id}")
    fun deleteEvent(@PathVariable id: Long): Any {
        val note = repository.findById(id)
        repository.removeById(id)
        return note
    }

    fun eventsView(events: List<Event>, currentUser: User) = mapOf("events" to events.map({ event: Event -> eventToData(event) }))

    fun eventToData(event: Event): EventData {
        return EventData(event.title, format.format(event.start), format.format(event.end), event.id)
    }

    fun dataToEvent(data: EventData): Event {
        return Event(data.title!!,
                java.sql.Date(format.parse(data.start).time),
                java.sql.Date(format.parse(data.end).time),
                userService.currentUser())
    }
}