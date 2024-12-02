import kotlin.math.abs

fun main() {
    //Testing lines - If any fails, it throws an error
    val test = numberInput("Day02_test")
    check(day2Part1(test) == 2)
    check(day2Part2(test) == 4)

    //Runs part 1 and 2
    val input = numberInput("Day02")
    day2Part1(input).println()
    day2Part2(input).println()
}

fun day2Part1(result: List<MutableList<Int>>) = result.count { isSafe(it) }
fun day2Part2(result: List<MutableList<Int>>) = result.count {
    when {
        !isSafe(it) -> it.withIndex().any{ (i, _) -> isSafe(it.dropAt(i))}
        else -> true
    }
}

fun isSafe(list: List<Int>) =  isIncrementedWithinRange(list, 1..3) && isOrdered(list)
fun isIncrementedWithinRange(list: List<Int>, difference:IntRange) = list.zipWithNext().all{ abs(it.second - it.first) in difference }