package io.realworld

import io.realworld.model.Group
import io.realworld.model.News
import io.realworld.model.User
import io.realworld.repository.GroupRepository
import io.realworld.repository.NewsRepository
import io.realworld.repository.UserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.streams.asSequence

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsTests {

    @Autowired
    private var userRepository: UserRepository? = null
    @Autowired
    private var groupRepository: GroupRepository? = null
    @Autowired
    private var repository: NewsRepository? = null

    private var group: Group? = null
    private var news: News? = null

    private val user = getRandomUser()

    private final fun getRandomUser(): User {
        var name = ""
        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        name += Random().ints(9, 0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString("")
        return User(name + "@mail.ru",
                "\$2a\$10\$7zEkCC8e9797I1W5o2LWK.0Mv0pzcr7UtJ25X.nDv3RX/7.btpDny",
                "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1MTk2NDg0NzMsInN1YiI6ImFzZGZAbWFpbC5ydSIsImlzcyI6IktvdGxpbiZTcHJpbmciLCJleHAiOjE1MjA1MTI0NzN9.ihDzGLU-1kUY1CEa80a-xZiFPYYZbTk-tW43fR6J_-w",
                name)
    }

    @Before
    fun before() {
        group = Group("KusKusKlan", listOf(user))
        news = News("Something big happened", "lololo", group!!)
        repository!!.save(news)

        groupRepository!!.save(group)
        user.groups = listOf(group!!)
        Assert.assertNotNull(groupRepository)
        Assert.assertNotNull(user)
        Assert.assertNotNull(group)
        Assert.assertNotNull(userRepository)
        userRepository!!.save(user)

    }

    @Test
    fun testFindByGroup() {
        val groupNews = repository!!.findAllByGroup(group!!)

        Assert.assertTrue("Could not get group news", groupNews.find { (title) -> title == news!!.title } != null)
    }

    @Test
    fun testFindById() {
        Assert.assertTrue(repository!!.findById(1).isPresent)
    }

    @Test
    fun testFindByTitle() {
        val foundNews = repository!!.findByTitle(news!!.title)[0]
        Assert.assertTrue(foundNews.title == news!!.title)
    }
}