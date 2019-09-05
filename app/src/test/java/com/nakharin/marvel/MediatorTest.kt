package com.nakharin.marvel

import org.junit.Test

class MediatorTest {

    @Test
    fun testMediator() {
        val mediator = ChatMediator()

        val john = ChatUser(mediator, "John")
        val alice = ChatUser(mediator, "Alice")
        val bob = ChatUser(mediator, "Bob")

        mediator
            .addUser(alice)
            .addUser(bob)
            .addUser(john)

        john.send("Hi everyone!")

        alice.send("Hi John!")
        bob.send("Hi John!")
    }
}

class ChatUser(private val mediator: ChatMediator, private val name: String) {
    fun send(msg: String) {
        println("$name: Sending Message= $msg")
        mediator.sendMessage(msg, this)
    }

    fun receive(msg: String) {
        println("$name: Message received: $msg")
    }
}

class ChatMediator {

    private val users: MutableList<ChatUser> = arrayListOf()

    fun sendMessage(msg: String, user: ChatUser) {
        users
            .filter { it != user }
            .forEach {
                it.receive(msg)
            }
    }

    fun addUser(user: ChatUser): ChatMediator {
        return apply {
            users.add(user)
        }
    }
}