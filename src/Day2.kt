import kotlin.math.abs

fun main() {
    fun part1(result: List<MutableList<Int>>) = result.count { isSafe(it) }
    fun part2(result: List<MutableList<Int>>) = result.count {
        when {
            !isSafe(it) -> it.withIndex().any{ (i, _) -> isSafe(it.dropAt(i))}
            else -> true
        }
    }

    //Testing lines - If any fails, it throws an error
    val test = numberInput(2, test = true)
    check(part1(test) == 2)
    check(part2(test) == 4)

    //Runs part 1 and 2
    val input = numberInput(2)
    part1(input).println()
    part2(input).println()
}

fun isSafe(list: List<Int>) =  isIncrementedWithinRange(list, 1..3) && isOrdered(list)
fun isIncrementedWithinRange(list: List<Int>, difference:IntRange) = list.zipWithNext().all{ abs(it.second - it.first) in difference }