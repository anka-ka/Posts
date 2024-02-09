data class Chats(
    var date: Long = System.currentTimeMillis() / 1000L,
    var isDeleted: Boolean = false,
    var messages: ArrayList<Messages> = ArrayList()
)

data class Messages(
    val interlocutorId: Int = 1,
    var isRead: Boolean = false,
    var isDeleted: Boolean = false,
    val text: String = "Текст сообщения",
    var date: Long = System.currentTimeMillis() / 1000L,
    var count: Int = 0
)

object ChatService {
    private var uniqueId = 0
    var chats = ArrayList<Chats>()
    fun clear() {
        chats = ArrayList<Chats>()
        uniqueId = 0
    }

    fun addChat(chat: Chats, interlocutorId: Int, messages: List<String>): Chats {
        val newChat = Chats()
        val messageList = ArrayList<Messages>()

        for (messageText in messages) {
            val newMessage = Messages(interlocutorId = interlocutorId, text = messageText)
            chat.messages.add(newMessage)
            messageList.add(newMessage)
        }
        newChat.messages.addAll(messageList)
        chats.add(newChat)
        return newChat
    }

    fun addMessagesToChat(chat: Chats, messages: List<Pair<Int, String>>): List<Messages> {
        val addedMessages = mutableListOf<Messages>()

        for ((index, pair) in messages.withIndex()) {
            val (interlocutorId, messageText) = pair
            val newMessage = Messages(interlocutorId = interlocutorId, text = messageText)
            chat.messages.add(newMessage)
            addedMessages.add(newMessage)
        }
        return addedMessages
    }

    fun getChats(): List<Pair<Int, List<String>>> {
        if (chats.isEmpty()) {
            throw ChatNotFoundException("Нет чатов")
        }
        val allChats = mutableListOf<Pair<Int, List<String>>>()
        for (chat in chats) {
            val firstMessage = chat.messages.firstOrNull() ?: continue
            val interlocutorId = firstMessage.interlocutorId
            val messages = chat.messages.map { it.text }
            allChats.add(Pair(interlocutorId, messages))
        }
        return allChats
    }

    class ChatNotFoundException(message: String) : Throwable(message)

    fun deleteMessage(chat: Chats, messageText: String): Boolean {
        val message = chat.messages.find { it.text == messageText }
        return if (message != null) {
            message.isDeleted = true
            true
        } else {
            throw DeletedMessageNotFoundException("Сообщение с текстом \"$messageText\" для удаления не найдено")
        }
    }

    class DeletedMessageNotFoundException(message: String) : Throwable(message)

    fun messageIsRead(chat: Chats, messageText: String): Boolean {
        val message = chat.messages.find { it.text == messageText }
        return if (message != null) {
            message.isRead = true
            true
        } else {
            throw ReadMessageNotFoundException("Сообщение с текстом \"$messageText\" не найдено")
        }
    }

    class ReadMessageNotFoundException(message: String) : Throwable(message)

    fun getUnreadChats(chats: List<Chats>): List<Chats> {
        val unreadChats = chats.filter { chat ->
            chat.messages.any { !it.isRead && !it.isDeleted }
        }
        if (unreadChats.isEmpty()) {
            throw NotFoundUnreadChatsException("Нет непрочитанных чатов")
        }
        return unreadChats
    }

    class NotFoundUnreadChatsException(message: String) : Throwable(message)

    fun getMessagesFromChat(chat: Chats, interlocutorId: Int): List<Messages> {
        val messages = chat.messages.filter { it.interlocutorId == interlocutorId }
        messages.forEach { it.isRead = true }
        return messages
    }

    fun getLastMessageFromChats(chat: Chats, interlocutorId: Int): Messages? {
        val messagesForInterlocutor = chat.messages.filter { it.interlocutorId == interlocutorId }
        return messagesForInterlocutor.lastOrNull { !it.isDeleted }
    }

    fun deleteChat(interlocutorId: Int): Boolean {
        val chatIndex = chats.indexOfFirst { chat ->
            chat.messages.any { message ->
                message.interlocutorId == interlocutorId
            }
        }
        if (chatIndex == -1) {
            throw NotFoundChatsException("Чат с interlocutorId $interlocutorId не найден")
        }
        val deletedChat = chats.removeAt(chatIndex)
        deletedChat.messages.forEach { message ->
            message.isDeleted = true
        }
        return true
    }

    class NotFoundChatsException(message: String) : Throwable(message)
}
