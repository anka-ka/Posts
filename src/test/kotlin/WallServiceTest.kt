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
        val result = wallService.add(Post(text  = "Тестирую функцию добавления"))
        assertEquals(1, result.id)
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
        val post1 = wallService.add(Post(text= "Тестирую функцию обновления"))
        val postToUpdate = Post(id = 3, text = " Нет такого id")
        val result = WallService.update(postToUpdate)
        assertFalse(result)
    }
}