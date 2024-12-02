import kotlin.math.abs

fun main() {
    //Testing lines - If any fails, it throws an error
    val (testLeft, testRight) = numberInputColumnGrouped("Day01_test", true)
    check(day1Part1(testLeft.zip(testRight)) == 11)
    check(day1Part2(testLeft,testRight) == 31L)

    //Runs part 1 and 2
    val (left,right) = numberInputColumnGrouped("Day01", true)
    day1Part1(left.zip(right)).println()
    day1Part2(left, right).println()
}

fun day1Part1(pair: List<Pair<Int,Int>>) = pair.fastSum { abs(it.first - it.second) }
fun day1Part2(left: MutableList<Int>, right: MutableList<Int>) = left.fastSumLong { it * right.count{ a -> a == it} }