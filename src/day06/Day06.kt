package day06

import println
import readInput

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Int {
        val timeLimits = input.first().split(":").last().split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toInt() }
        val distanceList = input.last().split(":").last().split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toInt() }

        return timeLimits.indices.asSequence().map { timeLimits[it] to distanceList[it] } //y to g
            .map {
                val sqrtd = sqrt((it.first).toDouble().pow(2.0) - 4.0 * it.second.toDouble())
                (it.first + sqrtd)/2.0 to (it.first - sqrtd)/2.0
            }

            .map { ceil(it.first).toInt() to floor(it.second).toInt() }
            .map { kotlin.math.abs(it.second - it.first + 1) }
            .reduce {acc, elem -> acc * elem}
    }

    fun part2(input: List<String>): Long {
        val timeLimit = input.first().split(":").last().replace(" ", "").toLong()
        val distance = input.last().split(":").last().replace(" ", "").toLong()
        val sqrtd = sqrt((timeLimit).toDouble().pow(2.0) - 4.0 * distance.toDouble())
        val x1 = floor((timeLimit + sqrtd)/2.0).toInt()
        val x2 = ceil((timeLimit - sqrtd)/2.0).toInt()
        return kotlin.math.abs(x2 - x1) + 1L
    }

    check(part1(readInput("day06/Day06_test")) == 288)
    check(part2(readInput("day06/Day06_test")) == 71503L)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}
