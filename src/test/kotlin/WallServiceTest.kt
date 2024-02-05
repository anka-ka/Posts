import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class WallServiceTest {
    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun add() {
        val wallService = WallService
        val result = wallService.add(Post(text = "Тестирую функцию добавления"))
        assertEquals(1, result.postId)
    }

    @Test
    fun updateSuccessfully() {
        val wallService = WallService
        val post1 = wallService.add(Post(text = "Тестирую функцию обновления"))
        val result = wallService.update(post1)
        assertTrue(result)
    }

    @Test
    fun updateUnsuccessfully() {

        val wallService = WallService
        val post1 = wallService.add(Post(text = "Тестирую функцию обновления"))
        val postToUpdate = Post(postId = 3, text = " Нет такого id")
        val result = WallService.update(postToUpdate)
        assertFalse(result)
    }

    @Test()
    fun createComment() {
        val wallService = WallService
        val post1 = wallService.add(Post(text = "Тестирую функцию создания комментария к посту"))
        val addedComment = WallService.createComment(postId = 1, commentText = Comments(commentText = "ok"))
        val result = WallService.createComment(1, addedComment)
        assertEquals("ok", result.commentText)
    }

    @Test(expected = WallService.PostNotFoundException::class)
    fun shouldThrow() {
        val wallService = WallService
        wallService.createComment(
            postId = 1,
            commentText = Comments(commentText = "Тестирую функцию создания комментария к посту")
        )
    }
}