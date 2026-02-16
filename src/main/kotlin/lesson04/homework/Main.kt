package lesson04.homework

import kotlin.random.Random

class Inventory(val items: List<String> = emptyList()) {

    infix operator fun plus(string: String): Inventory = Inventory(items + string)

    infix operator fun get(i: Int): String = items[i]

    infix operator fun contains(item: String): Boolean = items.contains(item)
}

data class Toggle(val enabled: Boolean = false) {
    operator fun not() = Toggle(!enabled)
}

class Price(val amount: Int) {
    infix operator fun times(multiplier: Int) = Price(amount * multiplier)
}

class Step(val number: Int) {
    infix operator fun rangeTo(other: Step) = number..other.number
}

operator fun IntRange.contains(step: Step) = step.number in this

class Log(val entries: List<String>) {
    infix operator fun plus(other: Log) = Log(entries + other.entries)
}

class Person(private val name: String) {

    private val phrases = mutableListOf<String>()

    fun print() {
        println(phrases.joinToString(" "))
    }

    private fun selectPhrase(first: String, second: String): String {
        val random = Random.nextInt(0, 2)
        return if (random == 0) first else second
    }

    infix fun says(phrase: String): Person {
        phrases += phrase
        return this
    }

    infix fun and(phrase: String): Person {
        if (phrases.isEmpty()) throw IllegalStateException()
        return says(phrase)
    }

    infix fun or(phrase: String): Person {
        if (phrases.isEmpty()) throw IllegalStateException()
        phrases[phrases.lastIndex] = selectPhrase(phrases.last(), phrase)
        return this
    }
}

fun main() {
    val andrew = Person("Andrew")
    andrew says "Hello" and "brothers." or "sisters." and "I believe" and "you" and "can do it" or "can't"
    andrew.print()
}
