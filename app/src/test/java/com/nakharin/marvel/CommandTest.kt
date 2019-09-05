package com.nakharin.marvel

import org.junit.Test

class CommandTest {

    @Test
    fun testCommand() {
        CommandProcessor()
            .addToQueue(OrderAddCommand(1L))
            .addToQueue(OrderAddCommand(2L))
            .addToQueue(OrderPayCommand(2L))
            .addToQueue(OrderPayCommand(1L))
            .processCommands()
    }
}

interface OrderCommand {
    fun execute()
}

class OrderAddCommand(private val id: Long): OrderCommand {

    override fun execute() {
        println("Adding order with id: $id")
    }
}

class OrderPayCommand(private val id: Long): OrderCommand {

    override fun execute() {
        println("Paying for order with id: $id")
    }
}

class CommandProcessor {

    private val queue: ArrayList<OrderCommand> = arrayListOf()

    fun addToQueue(orderCommand: OrderCommand): CommandProcessor {
        return apply {
            queue.add(orderCommand)
        }
    }

    fun processCommands(): CommandProcessor {
        return apply {
            queue.forEach { it.execute() }
            queue.clear()
        }
    }
}

