package day03

import println
import readInput
import kotlin.collections.ArrayList

fun main() {
    fun part1(scheme: List<String>): Int {

        val numbers = ArrayList<Int>()
        for (lineIndex in scheme.indices) {
            var charIndex = 0
            while (charIndex < scheme[lineIndex].length) {
                val currentCharacter = scheme[lineIndex][charIndex]
                if (currentCharacter in '0'..'9') {
                    val numberWithLength = getNumberWithSymbolNeighbour(lineIndex, charIndex, scheme)
                    if (numberWithLength.third) {
                        numbers.add(numberWithLength.first)
                    }
                    charIndex += numberWithLength.second
                }
                charIndex++
            }
        }
        return numbers.reduce {acc, elem -> acc + elem}
    }

    fun part2(scheme: List<String>): Int {

        val symbolsWithNumbers = HashMap<Symbol, ArrayList<Int>>()
        for (lineIndex in scheme.indices) {
            var charIndex = 0
            while (charIndex < scheme[lineIndex].length) {
                val currentCharacter = scheme[lineIndex][charIndex]
                if (currentCharacter in '0'..'9') {
                    val numberWithLength = getNumberWithSymbols(lineIndex, charIndex, scheme)
                    if (numberWithLength.third.isEmpty()) {
                        continue;
                    }
                    numberWithLength.third
                        .filter { it.character == '*' }
                        .forEach {
                        if (!symbolsWithNumbers.containsKey(it)) {
                            symbolsWithNumbers[it] = ArrayList()
                        }
                        symbolsWithNumbers[it]!!.add(numberWithLength.first)

                    }
                    charIndex += numberWithLength.second
                }
                charIndex++
            }
        }
        return symbolsWithNumbers
            .filter { it.value.size >= 2 }
            .values
            .map { it.reduce { acc, i -> acc*i } }
            .reduce {acc, elem -> acc + elem}
    }

    check(part1(readInput("day03/Day03_test")) == 4361)
    check(part2(readInput("day03/Day03_test")) == 467835)

    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()

}

fun getNeighbours(row: Int, column: Int, scheme: List<String>): List<Symbol> {
    return listOf(
        row-1 to column-1,
        row-1 to column,
        row-1 to column+1,
        row to column-1,
        row to column+1,
        row+1 to column-1,
        row+1 to column,
        row+1 to column+1
        ).filter { it.first in scheme.indices && it.second in 0..<scheme.first().length}
        .map { Symbol(scheme [it.first][it.second], it.first, it.second) }
}

fun hasSymbolAround(row: Int, column: Int, scheme: List<String>): Boolean {
    return getNeighbours(row, column, scheme)
        .any { it.character !in '0'..'9' && it.character != '.' }
}

fun getNumberWithSymbolNeighbour(row: Int, column: Int, scheme: List<String>): Triple<Int, Int, Boolean> {
    val numberString = StringBuilder()
    var isValid = false
    for (i in column..<scheme.first().length) {
        val current = scheme[row][i]
        if (!current.isDigit()) {
            break
        }
        numberString.append(current)
        if (hasSymbolAround(row, i, scheme)) {
            isValid = true
        }
    }
    return Triple(numberString.toString().toInt(), numberString.length, isValid)
}

fun getNumberWithSymbols(row: Int, column: Int, scheme: List<String>): Triple<Int, Int, Set<Symbol>> {
    val numberString = StringBuilder()
    val symbols = HashSet<Symbol>()
    for (i in column..<scheme.first().length) {
        val current = scheme[row][i]
        if (!current.isDigit()) {
            break
        }
        numberString.append(current)
        symbols.addAll(getNeighbours(row, i, scheme))
    }
    return Triple(numberString.toString().toInt(), numberString.length, symbols)
}

class Symbol(val character: Char, val row: Int, val column: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Symbol

        if (character != other.character) return false
        if (row != other.row) return false
        if (column != other.column) return false

        return true
    }

    override fun hashCode(): Int {
        var result = character.hashCode()
        result = 31 * result + row
        result = 31 * result + column
        return result
    }
}
