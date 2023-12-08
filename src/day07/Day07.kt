package day07

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val order = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
        val hands = input.map {
            val parts = it.split(" ")
            Hand(parts.first(), parts.last().toInt())
        }
        val sortedHands = hands.sortedWith(HandComparator(order))
        var result = 0
        for (i in hands.indices) {
            result += sortedHands[i].bid * (hands.size - i)
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val order = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
        val customGrouping: (CharArray) -> List<Pair<Char, Int>> = {
            val stdGroup = it.groupBy { e -> e }
                    .map { e -> e.key to e.value.size }
                    .sortedBy { e -> e.second }
                    .reversed()
                    .toMutableList()
            val joker = stdGroup.find { e -> e.first == 'J' }
            if (joker?.second != 5) {
                stdGroup.remove(joker)
                stdGroup[0] = stdGroup.first().first to stdGroup.first().second + (joker?.second ?: 0)
            }
            stdGroup.toList()
        }

        val hands = input.map {
            val parts = it.split(" ")
            Hand(parts.first(), parts.last().toInt(), customGrouping)
        }
        val sortedHands = hands.sortedWith(HandComparator(order))
        var result = 0
        for (i in hands.indices) {
            result += sortedHands[i].bid * (hands.size - i)
        }
        return result
    }

    check(part1(readInput("day07/Day07_test")) == 6440)
    check(part2(readInput("day07/Day07_test")) == 5905)

    val input = readInput("day07/Day07")
    part1(input).println()
    part2(input).println()
}

enum class HandType(val priority: Int) {
    FIVE_OF_KIND(1),
    FOUR_OF_KIND(2),
    FULL_HOUSE(3),
    THREE_OF_KIND(4),
    TWO_PAIR(5),
    ONE_PAIR(6),
    HIGH_CARD(7)
}

class Hand(val cards: String,
           val bid: Int,
           grouping: (CharArray) -> List<Pair<Char, Int>> = {
               it.groupBy { e -> e }
                       .map { e -> e.key to e.value.size }
                       .sortedBy { e -> e.second }
                       .reversed()
           }) {

    private val groups = grouping(cards.toCharArray())

    private val handType = calculateHandType()
    
    private fun calculateHandType(): HandType {
        return when {
            groups.first().second == 5 -> HandType.FIVE_OF_KIND
            groups.first().second == 4 -> HandType.FOUR_OF_KIND
            groups.first().second == 3 && groups[1].second == 2 -> HandType.FULL_HOUSE
            groups.first().second == 3 -> HandType.THREE_OF_KIND
            groups.first().second == 2 && groups[1].second == 2 -> HandType.TWO_PAIR
            groups.first().second == 2 -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }
    
    fun getHandType(): HandType {
        return handType
    }

    override fun toString(): String {
        return "$cards -> $bid -> $handType"
    }
}

class HandComparator(private val order: List<Char>) : Comparator<Hand> {
    override fun compare(p0: Hand?, p1: Hand?): Int {
        val c = compareValues(p0?.getHandType()?.priority, p1?.getHandType()?.priority)
        if (c != 0) {
            return c
        }
        val orderWithValues = order.associateWith { order.indexOf(it) }
        for (i in 0..4) {
            val r = compareValues(orderWithValues[p0!!.cards[i]], orderWithValues[p1!!.cards[i]])
            if (r != 0) return r
        }
        return 0
    }
}
