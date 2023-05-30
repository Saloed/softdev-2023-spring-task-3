package com.example.test

import com.example.test.data.Author
import com.example.test.data.ChatElement
import com.example.test.data.Message
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun ChatElementAdditionTest() {
        val chat = ChatElement("123", "name", "", "")
        chat.addMessage(Message("", Author("", "")))
        chat.addMessage(Message("", Author("", "")))
        assertEquals(2, chat.messages.size)
        val chat2 = ChatElement("1234", "name", "", "")
        chat.addMessage(Message("", Author("", "")))
        chat.addMessage(Message("", Author("", "")))
        assertEquals(1, chat.messages.size)
    }
}