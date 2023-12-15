package day15

import println
import readInput
import java.util.LinkedList

fun main() {
    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf { hash(it) }
    }

    fun part2(input: List<String>): Int {
        val boxes = HashMap<Int, LinkedList<Lens>>()
        input.first().split(",")
            .forEach {
                if (it.contains("=")) {
                    val parts = it.split("=")
                    val lens = Lens(parts.first(), parts.last().toInt())
                    val box = boxes.getOrPut(hash(lens.label)) {LinkedList()}
                    val lensToReplace = box.find { l -> l == lens }
                    if (lensToReplace != null) {
                        lensToReplace.focalLength = lens.focalLength
                    } else {
                        box.add(lens)
                    }
                } else {
                    val label = it.trim('-')
                    boxes[hash(label)]?.removeIf{ lens -> lens.label == label }
                }
            }

        return boxes.map {
            it.value.mapIndexed {index, lens ->
                (it.key+1) * (index+1) * lens.focalLength
            }.sum()
        }.sum()
    }
    check(part1(readInput("day15/Day15_test")) == 1320)
    check(part2(readInput("day15/Day15_test")) == 145)

    val input = readInput("day15/Day15")
    part1(input).println()
    part2(input).println()
}

fun hash(value: String): Int {
    return value.toCharArray()
        .fold(0) {acc, elem ->
            ((acc + elem.code) * 17) % 256
        }
}

class Lens(val label: String, var focalLength: Int){
    override fun toString(): String {
        return "($label; $focalLength)"
    }

    override fun hashCode(): Int {
        return hash(label)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Lens

        return label == other.label
    }
}
