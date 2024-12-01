import kotlin.math.abs

fun main() {
    val (testLeft,testRight) = setup("Day01_test")
    check(part1(testLeft.zip(testRight)) == 11)
    check(part2(testLeft,testRight) == 31L)

    val (left,right) = setup("Day01")
    part1(left.zip(right)).println()
    part2(left, right).println()
}

fun setup(file: String): Pair<MutableList<Int>, MutableList<Int>> {
    val input = readInput(file)
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    input.fastForEach {
        it.split(" ").apply {
            left += first().toInt()
            right += last().toInt()
        }
    }
    left.sort()
    right.sort()

    return Pair(left,right)
}

fun part1(pair: List<Pair<Int,Int>>) = pair.fastSumBy { abs(it.first - it.second) }

fun part2(left: MutableList<Int>, right: MutableList<Int>): Long {
    var similarity = 0L
    left.fastForEach { i ->
        val amount = right.count{it == i}
        similarity += i * amount
    }

    return similarity
}
