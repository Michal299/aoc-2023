package day09

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { parseLine(it) }
            .map { extrapolateNextValue(it) }
            .reduce {acc, elem -> acc + elem}
    }

    fun part2(input: List<String>): Int {
        return input
            .map { parseLine(it) }
            .map { extrapolateFirstValue(it) }
            .reduce {acc, elem -> acc + elem}
    }

    check(part1(readInput("day09/Day09_test")) == 114)
    check(part2(readInput("day09/Day09_test")) == 2)

    val input = readInput("day09/Day09")
    part1(input).println()
    part2(input).println()
}

fun parseLine(line: String): List<Int> {
    return line.split(" ")
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .toList()
}

fun extrapolateNextValue(numbers: List<Int>): Int {
    if (numbers.all { it == 0 }) return 0
    val differences = numbers.windowed(2)
        .map { it[1] - it.first() }
    val nextDelta = extrapolateNextValue(differences)
    return numbers.last() + nextDelta
}

fun extrapolateFirstValue(numbers: List<Int>): Int {
    if (numbers.all { it == 0 }) return 0
    val differences = numbers.windowed(2)
        .map { it[1] - it.first() }
    val previousDelta = extrapolateFirstValue(differences)
    return numbers.first() - previousDelta
}