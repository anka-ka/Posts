fun main() {

    val post1 = WallService.add(Post(text =" Доброй ночи! "))
    println(post1)

    val post2 = WallService.add(Post(text = " С праздником! "))
    println(post2)

    val postToUpdate = Post(id = 1, text = " Не желаю доброй ночи")
    val isUpdated = WallService.update(postToUpdate)
    println(postToUpdate)

    if (isUpdated) {
        println("Пост обновлен")
    } else {
        println("Такого id не существует")
    }
}

data class Post(
    val id: Int = 0,
    val ownerId: Int = 2,
    val fromId: Int = 3,
    val createdBy: Int = 4,
    var date: Long = System.currentTimeMillis() / 1000L,
    var text: String? = " Доброе утро! ",
    var postType: String = "post",
    var isFavorite: Boolean = true,
    var isPinned: Boolean = false,
    var canEdit: Boolean = true,
    var comments: Comments = Comments()
    )

data class Comments (
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
        posts =  ArrayList<Post>()
        uniqueId = 0
    }
    fun add(post: Post): Post {
        val newPost = post.copy(id = ++uniqueId, comments = post.comments.copy())
        posts.add(newPost)
        return newPost
    }

    fun update(post: Post): Boolean {
        for (i in posts.indices) {
            if (posts[i].id == post.id) {
                posts[i] = post.copy(comments = post.comments.copy())
                return true
            }
        }
        return false
    }
}




