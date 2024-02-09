import org.junit.Before
import org.junit.Assert.*
import org.junit.Test

class ChatServiceTest {
    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun addChat() {
        val chatService = ChatService
        val chat = Chats()
        val message = listOf("Добавляю первое сообщение в первый чат")
        val result = chatService.addChat(chat, 1, message)
        assertEquals(message.size, result.messages.size)
        assertEquals(message[0], result.messages[0].text)
    }

    @Test
    fun addMessagesToChat() {
        val chatService = ChatService
        val chat = Chats()
        val message = listOf(Pair(1, "Привет!"))
        val result = chatService.addMessagesToChat(chat, message)
        assertEquals(message.size, result.size)
        assertEquals(message[0].second, result[0].text)
    }

    @Test
    fun getChats() {
        val chatService = ChatService
        val chat = Chats()
        val message1 = listOf("Добавляю первое сообщение в первый чат")
        val chat1 = chatService.addChat(chat, 1, message1)
        val message2 = listOf("Добавляю первое сообщение во второй чат")
        val chat2 = chatService.addChat(chat, 2, message2)
        val result = chatService.getChats()
        assertEquals(2, result.size)
        assertEquals(message1.size, chat1.messages.size)
        assertEquals(message2.size, chat2.messages.size)
    }

    @Test(expected = ChatService.ChatNotFoundException::class)
    fun shouldThrow() {
        val chatService = ChatService
        chatService.getChats()
    }

    @Test
    fun deleteMessage() {
        val chatService = ChatService
        val chat = Chats()
        val message = listOf("Добавляю первое сообщение в первый чат")
        val chat1 = chatService.addChat(chat, 1, message)
        val result = chatService.deleteMessage(chat, "Добавляю первое сообщение в первый чат")
        assertTrue(result)
    }

    @Test(expected = ChatService.DeletedMessageNotFoundException::class)
    fun shouldThrowDeletedMessageNotFoundException() {
        val chatService = ChatService
        val chat = Chats()
        val message = listOf("Добавляю первое сообщение в первый чат")
        val chat1 = chatService.addChat(chat, 1, message)
        val result = chatService.deleteMessage(chat, "Не то сообщение")
    }

    @Test
    fun messageIsRead() {
        val chatService = ChatService
        val chat = Chats()
        val message = listOf("Добавляю первое сообщение в первый чат")
        val chat1 = chatService.addChat(chat, 1, message)
        val result = chatService.messageIsRead(chat, "Добавляю первое сообщение в первый чат")
        assertTrue(result)
    }

    @Test(expected = ChatService.ReadMessageNotFoundException::class)
    fun shouldThrowReadMessageNotFoundException() {
        val chatService = ChatService
        val chat = Chats()
        val message = listOf("Добавляю первое сообщение в первый чат")
        val chat1 = chatService.addChat(chat, 1, message)
        val result = chatService.messageIsRead(chat, "Не то сообщение")
    }

    @Test
    fun getUnreadChats() {
        val chatService = ChatService
        val chat1 = Chats(messages = arrayListOf(Messages(isRead = false)))
        val chat2 = Chats(messages = arrayListOf(Messages(isRead = true)))
        chatService.chats.addAll(listOf(chat1, chat2))
        val result = chatService.getUnreadChats(chatService.chats)
        assertEquals(1, result.size)
        assertEquals(chat1.messages, result[0].messages)
    }

    @Test(expected = ChatService.NotFoundUnreadChatsException::class)
    fun shouldThrowNotFoundUnreadChatsException() {
        val chatService = ChatService
        val chat1 = Chats(messages = arrayListOf(Messages(isRead = true)))
        val chat2 = Chats(messages = arrayListOf(Messages(isRead = true)))
        chatService.chats.addAll(listOf(chat1, chat2))
        val result = chatService.getUnreadChats(chatService.chats)
        assertEquals(1, result.size)
    }

    @Test
    fun getMessagesFromChat() {
        val chatService = ChatService
        val chat = Chats()
        val message1 = Messages(interlocutorId = 1, text = "Сообщение 1")
        val message2 = Messages(interlocutorId = 2, text = "Сообщение 2")
        val message3 = Messages(interlocutorId = 1, text = "Сообщение 3")
        val messagesToAdd = listOf(message1, message2, message3)
        chat.messages.addAll(messagesToAdd)
        val result = chatService.getMessagesFromChat(chat, 1)
        assertEquals(2, result.size)
        assertEquals("Сообщение 1", result[0].text)
        assertEquals("Сообщение 3", result[1].text)
        assertTrue(result.all { it.isRead })
    }

    @Test
    fun testGetLastMessageFromChats() {
        val chatService = ChatService
        val chat = Chats()
        val message1 = Messages(interlocutorId = 1, text = "Сообщение 1")
        val message2 = Messages(interlocutorId = 2, text = "Сообщение 2")
        val message3 = Messages(interlocutorId = 1, text = "Сообщение 3")
        chat.messages.addAll(listOf(message1, message2, message3))
        val result = chatService.getLastMessageFromChats(chat, 1)
        assertEquals("Сообщение 3", result?.text)
    }

    @Test
    fun deleteChat() {
        val chatService = ChatService
        val chat = Chats()
        val chat1 = Chats(messages = arrayListOf(Messages(interlocutorId = 1)))
        val chat2 = Chats(messages = arrayListOf(Messages(interlocutorId = 2)))
        chatService.chats.addAll(listOf(chat1, chat2))
        val result = chatService.deleteChat(1)
        assertTrue(result)
        assertTrue(chat1.messages.all { it.isDeleted })
    }

    @Test(expected = ChatService.NotFoundChatsException::class)
    fun shouldThrowNotFoundChatsException() {
        val chatService = ChatService
        val chat = Chats()
        val chat1 = Chats(messages = arrayListOf(Messages(interlocutorId = 1)))
        val chat2 = Chats(messages = arrayListOf(Messages(interlocutorId = 2)))
        chatService.chats.addAll(listOf(chat1, chat2))
        val result = chatService.deleteChat(3)
    }
}

