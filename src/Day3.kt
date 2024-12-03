fun main(){
    fun part1(): Int {return 0}
    fun part2(): Int {return 0}

    //Testing lines - If any fails, it throws an error
    val test = numberInputColumnGrouped(3, true, test = true)
    check(part1() == 0)
    check(part2() == 0)

    //Runs part 1 and 2
    val input = numberInputColumnGrouped(3, true)
    part1().println()
    part2().println()
}