package day08

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = CircularList(input.first().toCharArray().toList())
        val map = input.subList(2, input.size).associate {
            val source = it.split("=").first().trim()
            val destinations = it.split("=")[1].trim().split(",")
                .map { dest ->
                    dest.trim().trim('(').trim(')')
                }
            source to Pair(destinations[0], destinations[1])
        }

        var steps = 0
        var current = "AAA"
        for (direction in instructions) {
            current = if (direction == 'L') map[current]!!.first else map[current]!!.second
            steps += 1
            if (current == "ZZZ") break
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val instructions = CircularList(input.first().toCharArray().toList())
        val map = input.subList(2, input.size).associate {
            val source = it.split("=").first().trim()
            val destinations = it.split("=")[1].trim().split(",")
                .map { dest ->
                    dest.trim().trim('(').trim(')')
                }
            source to Pair(destinations[0], destinations[1])
        }

        val partialLengths = map.filter { it.key.endsWith("A") }
            .map {
                var step = 0L
                var current = it.key
                for (direction in instructions) {
                    current = if (direction == 'L') map[current]!!.first else map[current]!!.second
                    step += 1
                    if (current.endsWith('Z')) break
                }
                step
            }

        return lcm(partialLengths)
    }

    lcm(listOf(2, 3, 4, 5, 6)).println()
    check(part1(readInput("day08/Day08_test")) == 6)
    check(part2(readInput("day08/Day08_test2")) == 6L)

    val input = readInput("day08/Day08")
    part1(input).println()
    part2(input).println()
}

fun lcm(input: List<Long>): Long {
    return input.fold(input.first()) {acc, elem -> lcm(acc, elem)}
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}

class CircularList<T>(private val baseList: List<T>) : Iterable<T> {

    override fun iterator(): Iterator<T> {
        return CircularIterator(baseList)
    }

    class CircularIterator<T>(private val baseIterable: Iterable<T>) : Iterator<T> {
        private var currentIterator = baseIterable.iterator()
        private var hasNextResult = baseIterable.count() > 0
        override fun hasNext() = hasNextResult

        override fun next(): T {
            if (!currentIterator.hasNext()) {
                currentIterator = baseIterable.iterator()
            }
            return currentIterator.next()
        }
    }
}