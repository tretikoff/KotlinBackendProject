//package io.realworld
//
//import io.realworld.model.Event
//import io.realworld.model.User
//import io.realworld.repository.EventRepository
//import io.realworld.repository.UserRepository
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringRunner
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.streams.asSequence
//
//@RunWith(SpringRunner::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class EventTests {
//
//    @Autowired
//    private var repository: EventRepository? = null
//    private var userRepository: UserRepository? = null
//    private var event: Event? = null
//    private var eventToRemove: Event? = null
//
//    private val format = SimpleDateFormat("yyyy-mm-dd")
//    private var user = getRandomUser()
//
//    private final fun getRandomUser(): User {
//        var name = ""
//        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//        name += Random().ints(9, 0, source.length)
//                .asSequence()
//                .map(source::get)
//                .joinToString("")
//        return User(name + "@mail.ru",
//                "\$2a\$10\$7zEkCC8e9797I1W5o2LWK.0Mv0pzcr7UtJ25X.nDv3RX/7.btpDny",
//                "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MTk2NDg0NzMsInN1YiI6ImFzZGZAbWFpbC5ydSIsImlzcyI6IktvdGxpbiZTcHJpbmciLCJleHAiOjE1MjA1MTI0NzN9.ihDzGLU-1kUY1CEa80a-xZiFPYYZbTk-tW43fR6J_-w",
//                name)
//    }
//
//    @Before
//    fun before() {
//        val start = java.sql.Date(format.parse("2018-02-02").time)
//        val end = java.sql.Date(format.parse("2018-02-02").time)
//        event = Event("Food party", start, end, user)
//        val newStart = java.sql.Date(format.parse("2018-02-02").time)
//        val newEnd = java.sql.Date(format.parse("2018-02-02").time)
//        eventToRemove = Event("lol", newStart, newEnd, user)
////        user = userRepository!!.findByUsername(user.username)!!
//        userRepository?.save(user)
////        repository!!.
//        repository!!.saveAll(arrayListOf(event, eventToRemove))
//    }
//
//
//    @Test
//    fun testFindByUser() {
//        val userEvents = repository!!.findAllByUser(user)
//        Assert.assertTrue("Could not get user events",
//                userEvents.find { (title) ->
//                    title == event!!.title
//                } != null)
//    }
//
//    @Test
//    fun testFindById() {
//        Assert.assertTrue(repository!!.findById(1).isPresent)
//    }
//
//    @Test
//    fun testFindAllByUser() {
//        val userEvents = repository!!.findAllByUser(user)
//        Assert.assertTrue(!userEvents.isEmpty())
//        Assert.assertTrue(userEvents[0].user.id == user.id)
//    }
//
//    @Test
//    fun testRemoveById() {
//        repository!!.removeById(eventToRemove!!.id)
//        Assert.assertFalse(repository!!.findById(eventToRemove!!.id).isPresent)
//    }
//
//    @Test
//    fun testUpdate() {
//        var title = "updated event"
//        event!!.title = title
//
//        var newEvent = repository!!.findById(event!!.id).get()
//        newEvent.title = title
//        repository!!.save(newEvent)
//        newEvent = repository!!.findById(event!!.id).get()
//        Assert.assertTrue(newEvent.title == title )
//    }
//}