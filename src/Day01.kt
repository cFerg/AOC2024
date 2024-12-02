import kotlin.math.abs

fun main() {
    //Testing lines - If any fails, it throws an error
    val (testLeft, testRight) = numberInputColumnGrouped("Day01_test", true)
    check(part1(testLeft.zip(testRight)) == 11)
    check(part2(testLeft,testRight) == 31L)

    //Runs part 1 and 2
    val (left,right) = numberInputColumnGrouped("Day01", true)
    part1(left.zip(right)).println()
    part2(left, right).println()
}

fun part1(pair: List<Pair<Int,Int>>) = pair.fastSum { abs(it.first - it.second) }
fun part2(left: MutableList<Int>, right: MutableList<Int>) = left.fastSumLong { it * right.count{ a -> a == it} }