import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow

fun main(){
    fun challenge(input: List<String>): Pair<Long,Long> {
        var part1 = 0L
        var part2 = 0L
        for (i in input){
            val (big, small) = i.split(":")

            val target = big.toLong()
            val smallList = small.trim().split(" ").map { it.toInt() }

            val (first, second) = canMakeTarget(smallList, target)
            when {
                first -> {
                    part1 += target
                    part2 += target
                }
                second -> {
                    part2 += target
                }
            }
        }

        return Pair(part1, part2)
    }

    //Testing lines - If any fails, it throws an error
    val (test1, test2) = challenge(stringInput(7, test = true))
    check(test1 == 3749L)
    check(test2 == 11387L)

    //Runs part 1 and 2
    val (part1,part2) = challenge(stringInput(7))
    "Part 1: $part1".println()
    "Part 2: $part2".println()
}

fun canMakeTarget(nums: List<Int>, target: Long): Pair<Boolean, Boolean> {
    fun result(a: Int, b: Long): Pair<Boolean, Boolean> {
        //If we're on the last index, check if any of the combinations are valid
        if (a == nums.size) {
            val r = b == target
            return Pair(r, r)
        }

        val c = nums[a]

        //Used for checking both part1 and part2
        val (add1, add2) = result(a + 1, b + c)
        val (times1, times2) = result(a + 1, b * c)

        //Used for checking part2
        val (_,concat) = if (a > 0) result(a + 1, b.join(c)) else Pair(false, false)

        val part1 = add1 || times1
        val part2 = add2 || times2 || concat

        return Pair(part1, part2)
    }

    // Start the recursion with the first indexed number
    return result(1, nums[0].toLong())
}

fun Int.join(other: Int) = this * 10.0.pow(other.digitCount()).toLong() + other
fun Long.join(other: Int) = this * 10.0.pow(other.digitCount()).toLong() + other
fun Int.digitCount() = if (this < 1_000_000_000 && this > -1_000_000_000){
    if (this == 0) 1 else {
        var count = 0
        var n = absoluteValue
        do {
            count++
            n /= 10
        } while (n > 0)
        count
    }}else{
        log10(absoluteValue.toDouble()).toInt() + 1
    }