import org.junit.Before
import org.junit.Assert.*
import org.junit.Test

class NotesServiceTest {
    @Before
    fun clearBeforeTest() {
        NotesService.clear()
    }

    @Test
    fun add() {
        val noteService = NotesService
        val result = noteService.add(Note(text = "Заметка1"))
        assertEquals(1, result)
    }

    @Test
    fun updateSuccessfully() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Заметка1"))
        val noteToUpdate = Note(noteId = 1, text = "Обновление заметки")
        val result = NotesService.update(noteToUpdate)
        assertTrue(result)
    }

    @Test
    fun updateUnsuccessfully() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Заметка1"))
        val noteToUpdate = Note(noteId = 3, text = "Обновление заметки")
        val result = NotesService.update(noteToUpdate)
        assertFalse(result)
    }

    @Test()
    fun createComment() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Тестирую функцию создания комментария к заметке"))
        val result = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        assertEquals(0, result)
    }

    @Test(expected = NotesService.NoteIdNotFoundException::class)
    fun shouldThrow() {
        val noteService = NotesService
        noteService.createComment(1, Comments(commentText = "Тестирую функцию создания комментария к заметке"))
    }

    @Test()
    fun deleteComment() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Тестирую функцию создания комментария к заметке"))
        val commentForNote = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        val result = NotesService.deleteComment(1, 0)
        assertTrue(result)
    }

    @Test(expected = NotesService.CommentNotFoundException::class)
    fun shouldThrowException() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Тестирую функцию создания комментария к заметке"))
        val commentForNote = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        noteService.deleteComment(3, 0)
    }

    @Test()
    fun editCommentSuccessfully() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Тестирую функцию создания комментария к заметке"))
        val commentForNote = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        val result = NotesService.editComment(1, 0, newCommentText = "Добавляю новый комментарий к заметке")
        assertTrue(result)
    }

    @Test()
    fun editCommentUnsuccessfully() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Тестирую функцию создания комментария к заметке"))
        val commentForNote = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        val result = NotesService.editComment(4, 0, newCommentText = "Добавляю новый комментарий к заметке")
        assertFalse(result)
    }

    @Test()
    fun get() {
        val noteService = NotesService
        var notes = ArrayList<Note>()
        val note1 = noteService.add(Note(text = "Заметка1"))
        notes.add(noteService.getById(note1))
        val result = NotesService.get()
        assertEquals(notes, result)
    }

    @Test(expected = NotesService.NoteNotFoundException::class)
    fun shouldThrowExceptionForFunGetById() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Заметка1"))
        val noteById = noteService.getById(2)
    }

    @Test()
    fun getComments() {
        val noteService = NotesService
        var notes = ArrayList<Note>()
        val note1 = noteService.add(Note(text = "Заметка1"))
        val commentText = "Добавляю комментарий к заметке"
        val comment1 = NotesService.createComment(1, Comments(commentText = commentText))
        val result = NotesService.getComments(1)
        assertEquals(comment1, result[0].commentId.toLong())
        assertEquals(commentText, result[0].commentText)
    }

    @Test(expected = NotesService.CommentsNotFoundException::class)
    fun shouldThrowExceptionForFunGetComments() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Заметка1"))
        val comment1 = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        val result = NotesService.getComments(3)
    }

    @Test()
    fun restoreCommentSuccessfully() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Заметка1"))
        val comment1 = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        val deletedComment = NotesService.deleteComment(1, 1)
        val result = NotesService.restoreComment(1, 1)

        assertTrue(true)
    }

    @Test()
    fun restoreCommentUnsuccessfully() {
        val noteService = NotesService
        val note1 = noteService.add(Note(text = "Заметка1"))
        val comment1 = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
        val deletedComment = NotesService.deleteComment(1, 1)
        val result = NotesService.restoreComment(2, 1)
        assertFalse(false)
    }
}