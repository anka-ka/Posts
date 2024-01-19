import org.junit.Test

import org.junit.Assert.*

class WallServiceTest {

    @Test
    fun add() {
        val wallService = WallService
        val result = wallService.add("Тестирую функцию добавления")
        assertEquals(1, result.id)
    }
    @Test
    fun updateSuccessfully() {
        val wallService = WallService
        val post1 = wallService.add("Тестирую функцию обновления")
        val result = wallService.update(post1)
        assertEquals(true, result)
    }
    @Test
    fun updateUnsuccessfully() {
        val wallService = WallService
        val post1 = wallService.add("Тестирую функцию обновления")
        val postToUpdate = Post(id = 2, text = " Нет такого id")
        val result = WallService.update(postToUpdate)
        assertEquals(false, result)
    }
}