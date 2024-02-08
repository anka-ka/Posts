interface CrudService<E> {
    fun add(entity: E): Long
    fun update(entity: E): Boolean
    fun delete(id: Long): Boolean
    fun deleteComment(entityId: Long, commentId: Long): Boolean
    fun editComment(entityId: Long, commentId: Long, newCommentText: String): Boolean
    fun get(): List<E>
    fun createComment(entityId: Long, comment: Comments): Long
    fun getById(id: Long): E
    fun getComments(entityId: Long): List<Comments>
    fun restoreComment(entityId: Long, commentId: Long): Boolean
    fun clear()
}