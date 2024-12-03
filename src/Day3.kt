fun main(){
    fun challenge(input: String): Pair<Long,Long> {
        val mulSearch = Regex("""mul\((\d{1,3}),(\d{1,3})\)|don't\(\)|do\(\)""")

        var multiply = true
        var part1 = 0L
        var part2 = 0L //Only add results when multiply is true

        mulSearch.findAll(input).forEach{ m ->
            when {
                m.value.startsWith("don't()") -> multiply = false
                m.value.startsWith("do()") -> multiply = true
                m.value.startsWith("mul") -> {
                    val (a,b) = m.destructured
                    val mul = a.toInt() * b.toInt()
                    part1 += mul
                    if (multiply) part2 += mul
                }
            }
        }

        return Pair(part1,part2)
    }

    //Testing lines - If any fails, it throws an error
    val test = stringInput(3, test = true).toString()
    check(challenge(test).first == 161L)
    check(challenge(test).second == 48L)

    //Runs part 1 and 2
    val input = stringInput(3).toString()
    challenge(input).first.println()
    challenge(input).second.println()
}