package day05

import println
import readInput
import java.util.*
import java.util.stream.Collectors
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input.first().split(":").last().trim().split(" ")
            .map { it.toLong() }

        val mappingsInOrder = parseToMappings(input.subList(2, input.size))
        return seeds.minOfOrNull {
            mappingsInOrder.fold(it) { acc, elem ->
                elem.map(acc)
            }
        } ?: 0
    }

    fun part2(input: List<String>): Long {
        val seedsPairs = input.first().split(":").last().trim().split(" ")
        val seeds = HashSet<Pair<Long, Long>>()
        val mappingsInOrder = parseToMappings(input.subList(2, input.size))
        for (i in seedsPairs.indices step 2) {
            seeds.add(seedsPairs[i].toLong() to (seedsPairs[i].toLong() + seedsPairs[i + 1].toLong() - 1L))
        }

        return seeds.parallelStream().map {
            var minValue = Long.MAX_VALUE
            for (i in it.first..it.second) {
                minValue = min(minValue, mappingsInOrder.fold(i) { acc, elem ->
                    elem.map(acc)
                })
            }
            minValue
        }.collect(Collectors.toSet()).min()
    }

    check(part1(readInput("day05/Day05_test")) == 35L)
    check(part2(readInput("day05/Day05_test")) == 46L)

    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()
}

class Mapping {
    private val ranges = LinkedList<Range>()

    fun addRange(range: Range) {
        ranges.add(range)
    }

    fun map(input: Long): Long {
        for (range in ranges) {
            if (range.isInRange(input)) {
                return range.map(input)
            }
        }
        return input
    }
}

class Range(outputStart: Long, private val inputStart: Long, private val size: Long) {
    private val change: Long = outputStart-inputStart

    fun isInRange(input: Long): Boolean {
        return input >= inputStart && input <= (inputStart + size)
    }

    fun map(input: Long): Long {
        return input + change
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Range

        if (inputStart != other.inputStart) return false
        if (size != other.size) return false
        if (change != other.change) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(inputStart, size, change)
    }
}

fun parseToMappings(input: List<String>): List<Mapping> {

    val mappings = LinkedList<Mapping>()
    var currentMapping = Mapping()
    for (line in input) {
        if (line.isBlank()) {
            mappings.add(currentMapping)
        } else if (line.first().isDigit()) {
            val parts = line.split(" ")
            val inputRangeStart = parts[1].toLong()
            val outputRangeStart = parts[0].toLong()
            val rangeLength = parts[2].toLong()
            currentMapping.addRange(Range(outputRangeStart, inputRangeStart, rangeLength))
        } else {
            currentMapping = Mapping()
        }
    }
    mappings.add(currentMapping)
    return mappings
}