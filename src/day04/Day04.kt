package day04

import println
import readInput
import java.util.Queue
import java.util.LinkedList

fun main() {
    fun part1(input: List<String>): Int {
         return input.stream()
             .map { mapGameToCard(it) }
             .map { card -> card.getPoints() }
             .reduce {acc, elem -> acc + elem}.get()
    }

    fun part2(input: List<String>): Int {
        val cards = input
            .map { mapGameToCard(it) }
            .associate { it.id to it.getMatchingCount()}

        val queue: Queue<Int> = LinkedList()
        cards.forEach{ queue.add(it.key) }

        var counter = 0
        while (queue.isNotEmpty()) {
            val cardId = queue.poll()
            counter++
            for (i in 1..(cards[cardId] ?: 0)) {
                if (cardId+i in cards.keys) {
                    queue.add(cardId+i)
                }
            }
        }
        return counter
    }

    check(part1(readInput("day04/Day04_test")) == 13)
    check(part2(readInput("day04/Day04_test")) == 30)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}

class Card(val id: Int, private val winningNumbers: Set<Int>, private val numbers: Set<Int>) {
    fun getMatchingCount(): Int {
        return winningNumbers.stream()
            .filter{ it in numbers}
            .count()
            .toInt()
    }

    fun getPoints(): Int {
        val matchingCount = getMatchingCount()
        return if (matchingCount == 0) {
            0
        } else {
            1 shl (matchingCount-1)
        }
    }
}

fun mapGameToCard(input: String): Card {
    val parts = input.split(":")
    val id = parts.first().split(" ").last().toInt()
    val allNumbers = parts.last().split("|")
    val winningNumbers = allNumbers.first().trim().split(" ")
        .filter(String::isNotBlank)
        .map {n -> n.trim().toInt() }
        .toSet()
    val cardNumbers = allNumbers.last().trim().split(" ")
        .filter(String::isNotBlank)
        .map {n -> n.trim().toInt() }
        .toSet()
    return Card(id, winningNumbers, cardNumbers)
}