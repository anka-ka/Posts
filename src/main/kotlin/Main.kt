fun main() {

    val post1 = WallService.add(Post(text = " Доброй ночи! "))
    println(post1)

    val post2 = WallService.add(Post(text = " С праздником! "))
    println(post2)

    val postToUpdate = Post(postId = 1, text = " Не желаю доброй ночи")
    val isUpdated = WallService.update(postToUpdate)
    println(postToUpdate)

    if (isUpdated) {
        println("Пост обновлен")
    } else {
        println("Такого id не существует")
    }
    val newComment = WallService.createComment(postId = 1, Comments(commentText = "ok"))
    println(newComment)
    val note1 = NotesService.add(Note(text = "Заметка1"))
    println(note1)
    val noteToUpdate = Note(noteId = 1, text = "Обновление заметки")
    val noteIsUpdated = NotesService.update(noteToUpdate)
    println(noteToUpdate)

    if (noteIsUpdated) {
        println("Заметка обновлена")
    } else {
        println("Такого id не существует")
    }
    val createCommentForNote = NotesService.createComment(1, Comments(commentText = "Добавляю комментарий к заметке"))
    println(createCommentForNote)
    val editedComment = NotesService.editComment(1, 0, newCommentText = "Добавляю новый комментарий к заметке")
    println(editedComment)
    println(NotesService.get())
    val noteGottenById = NotesService.getById(1)
    println("noteGottenById" + noteGottenById)
    println("get comments" + NotesService.getComments(1))
    val deleteCommentForNote = NotesService.deleteComment(1, 1)
    println("deleteCommentForNote " + deleteCommentForNote)
    val restoredCommentForNote = NotesService.restoreComment(1, 0)
    println(restoredCommentForNote)
    val noteIsDeleted = NotesService.delete(1)
    println(noteIsDeleted)

}

data class Note(
    val title: String = " Заголовок",
    val text: String = "Текст заметки",
    val noteId: Int = 1,
    val message: String = "Текст комментария",
    val commentId: Long = 1,
    var comments: ArrayList<Comments> = ArrayList(),
)

data class Post(
    val postId: Int = 0,
    val ownerId: Int = 2,
    val fromId: Int = 3,
    val createdBy: Int = 4,
    var date: Long = System.currentTimeMillis() / 1000L,
    var text: String? = " Доброе утро! ",
    var postType: String = "post",
    var isFavorite: Boolean = true,
    var isPinned: Boolean = false,
    var canEdit: Boolean = true,
    var comments: ArrayList<Comments> = ArrayList(),
    val attachments: Array<Attachment> = emptyArray()
)

data class Comments(
    val commentId: Int = 0,
    var date: Long = System.currentTimeMillis() / 1000L,
    var commentText: String? = " Отличный пост!",
    val count: Int = 15,
    var canPost: Boolean = true,
    var groupsCanPost: Boolean = true,
    var canClose: Boolean = false,
    var canOpen: Boolean = true,
    var isDeleted: Boolean = false
)

object WallService {
    private var uniqueId = 0
    private var posts = ArrayList<Post>()

    fun clear() {
        posts = ArrayList<Post>()
        uniqueId = 0
    }

    fun add(post: Post): Post {
        val newComments = ArrayList<Comments>(post.comments)
        val newPost = post.copy(postId = ++uniqueId, comments = newComments)
        posts.add(newPost)
        return newPost
    }

    fun update(post: Post): Boolean {
        for (i in posts.indices) {
            if (posts[i].postId == post.postId) {
                val newComments = ArrayList<Comments>(post.comments)
                posts[i] = post.copy(comments = newComments)
                return true
            }
        }
        return false
    }

    fun createComment(postId: Int, commentText: Comments): Comments {
        for (i in posts.indices) {
            if (posts[i].postId == postId) {
                val newComment = commentText.copy()
                posts[i].comments.add(newComment)
                return newComment
            }
        }
        throw PostNotFoundException("Пост с id $postId не найден")
    }

    class PostNotFoundException(message: String) : Throwable(message)
}

object NotesService : CrudService<Note> {
    private var uniqueId = 0
    private var notes = ArrayList<Note>()

    override fun clear() {
        notes = ArrayList<Note>()
        uniqueId = 0
    }

    override fun add(entity: Note): Long {
        val newNote = entity.copy(noteId = ++uniqueId)
        notes.add(newNote)
        return newNote.noteId.toLong()
    }

    override fun update(note: Note): Boolean {
        for (i in notes.indices) {
            if (notes[i].noteId == note.noteId) {
                val newComments = ArrayList<Comments>(note.comments)
                notes[i] = note.copy(comments = newComments)
                return true
            }
        }
        return false
    }

    override fun delete(id: Long): Boolean {
        for (i in notes.indices) {
            if (notes[i].noteId.toLong() == id) {
                notes.removeAt(i)
                return true
            }
        }
        return false
    }

    override fun createComment(noteId: Long, commentText: Comments): Long {
        for (i in notes.indices) {
            if (notes[i].noteId.toLong() == noteId) {
                val newComment = commentText.copy()
                notes[i].comments.add(newComment)
                return newComment.commentId.toLong()
            }
        }
        throw NoteIdNotFoundException("Заметка с id" + noteId + "не найдена")
    }

    class NoteIdNotFoundException(message: String) : Throwable(message)


    override fun deleteComment(entityId: Long, commentId: Long): Boolean {
        for (noteId in notes.indices) {
            val note = notes[noteId]
            if (note.noteId.toLong() == entityId) {
                for (commentId in note.comments.indices) {
                    val comment = note.comments[commentId]
                    if (comment.commentId == commentId) {
                        note.comments.removeAt(commentId)
                        comment.isDeleted = true
                        return true
                    }
                }
            }
        }
        throw CommentNotFoundException("Комментарий с id $commentId не найден")
    }

    class CommentNotFoundException(message: String) : Throwable(message)

    override fun editComment(entityId: Long, commentId: Long, newCommentText: String): Boolean {
        for (note in notes) {
            if (note.noteId.toLong() == entityId) {
                for (comment in note.comments) {
                    if (comment.commentId.toLong() == commentId) {
                        comment.commentText = newCommentText
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun get(): List<Note> {
        return notes
    }

    override fun getById(id: Long): Note {
        for (note in notes) {
            if (note.noteId.toLong() == id) {
                return note
            }
        }
        throw NoteNotFoundException("Заметка с id $id не найдена")
    }

    class NoteNotFoundException(message: String) : Throwable(message)

    override fun getComments(id: Long): List<Comments> {
        for (note in notes) {
            if (note.noteId.toLong() == id) {
                return note.comments
            }
        }
        throw CommentsNotFoundException("Заметка с id $id для получения комментариев к ней не найдена")
    }

    class CommentsNotFoundException(message: String) : Throwable(message)

    override fun restoreComment(entityId: Long, commentId: Long): Boolean {
        for (noteId in notes.indices) {
            val note = notes[noteId]
            if (note.noteId.toLong() == entityId) {
                for (commentId in note.comments.indices) {
                    val comment = note.comments[commentId]
                    if (comment.commentId == commentId && comment.isDeleted) {
                        comment.isDeleted = false
                        return true
                    }
                }
            }
        }
        return false
    }
}









