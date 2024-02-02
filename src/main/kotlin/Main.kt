
fun main() {

    val post1 = WallService.add(Post(text =" Доброй ночи! "))
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
    val newComment = WallService.createComment(postId = 1, commentText = Comments(commentText = "ok"))
    println(newComment)
}

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

data class Comments (
    val commentId: Int = 0,
    var date: Long = System.currentTimeMillis() / 1000L,
    val commentText: String? = " Отличный пост!",
    val count: Int = 15,
    var canPost: Boolean = true,
    var groupsCanPost: Boolean = true,
    var canClose: Boolean = false,
    var canOpen: Boolean = true
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







