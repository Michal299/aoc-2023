package day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.stream().map { word ->
            val digits = word.filter { character -> character.isDigit() }
            "${digits.first()}${digits.last()}".toInt()
        }.reduce { acc, elem -> acc+elem}.orElse(0)
    }

    fun part2(input: List<String>): Int {
        val digitsAsWords = mapOf(
            "one" to "o1e",
            "two" to "t2o",
            "three" to "t3e",
            "four" to "f4r",
            "five" to "f5e",
            "six" to "s6x",
            "seven" to "s7n",
            "eight" to "e8t",
            "nine" to "n9e"
        )

        fun replaceWordWithDigits(sentence: String): String {
            return digitsAsWords.entries
                .fold(sentence) { acc, elem -> acc.replace(elem.key, elem.value) }
        }

        return part1(input.map{replaceWordWithDigits(it)})
    }

    check(part1(readInput("day01/Day01_test")) == 142)
    check(part2(readInput("day01/Day01_test2")) == 281)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
