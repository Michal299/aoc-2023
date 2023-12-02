package day02

import println
import readInput
import java.util.stream.Collectors
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val config = GameRound(12, 13, 14)
        return input.stream()
            .map {
                val line = it.split(":")
                line.first().split(" ").last().toInt() to line.last()
            }
            .map { it.first to isGamePossible(it.second, config) }
            .filter {it.second}
            .map { it.first }
            .reduce(0) {acc, elem -> acc + elem}
    }

    fun part2(input: List<String>): Int {
         return input.stream()
            .map {
                val line = it.split(":")
                line.first().split(" ").last().toInt() to line.last()
            }
            .map { minimalConfigForGame(it.second) }
            .map { it.red * it.green * it.blue }
            .reduce(0) {acc, elem -> acc + elem}
    }

    check(part1(readInput("day02/Day02_test")) == 8)
    check(part2(readInput("day02/Day02_test")) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}

enum class Color(val text: String) {
    RED("red"),
    GREEN("green"),
    BLUE("blue")
}

class GameRound(val red: Int, val green: Int, val blue: Int)

fun isGamePossible(input: String, config: GameRound): Boolean {
    val maxValuesGame = maxConfig(input)
    return maxValuesGame.red <= config.red &&
           maxValuesGame.green <= config.green &&
           maxValuesGame.blue <= config.blue
}

fun minimalConfigForGame(input: String): GameRound {
    return maxConfig(input)
}

fun parseRound(input: String): GameRound {
    val parts = input.split(",")
        .stream()
        .map { it.trim() }
        .map {
            val subpart = it.split(" ")
            subpart.last() to subpart.first().toInt() 
        }
        .collect(Collectors.toMap({ it.first }, { it.second }))
    return GameRound(parts[Color.RED.text] ?: 0, parts[Color.GREEN.text] ?: 0, parts[Color.BLUE.text] ?: 0)
}

fun maxConfig(game: String): GameRound {
    return game.split(";").stream()
        .map(String::trim)
        .map { parseRound(it) }
        .reduce(GameRound(0, 0, 0)) { acc, elem ->
            GameRound(max(acc.red, elem.red),
                      max(acc.green, elem.green),
                      max(acc.blue, elem.blue))}
}
