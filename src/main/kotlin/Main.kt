fun main() {

    val post1 = WallService.add(" Доброй ночи! ")
    println(post1)

    val post2 = WallService.add(" С праздником! ")
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
    var text: String = " Доброе утро! ",
    var postType: String = "post",
    var isFavorite: Boolean = true,
    var isPinned: Boolean = false,
    var canEdit: Boolean = true,
    )

object comments {
    val count: Int = 15
    var canPost: Boolean = true
    var groupsCanPost: Boolean = true
    var canClose: Boolean = false
    var canOpen: Boolean = true
}

data class postList(
    val key: Int,
    val value: Post
)

object WallService {
    private var uniqueId = 1
    private var posts = ArrayList<postList>()
    fun add(post: String): Post {
        val newPost = Post(id = uniqueId++, text = post)
        val postEntry = postList(key = newPost.id, value = newPost)
        posts.add(postEntry)
        return newPost
    }

    fun update(post: Post): Boolean {
        for (entry in posts) {
            if (entry.key == post.id) {
                entry.value.text = post.text
                return true
            }
        }
        return false
    }
}




