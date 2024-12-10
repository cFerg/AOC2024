fun main(){
    fun challenge(input: List<Int>): Pair<Long,Long> {
        var part1 = 0L
        var part2 = 0L

        val part1Nums = IntArray(input.sum()).apply { fill(-1) }

        var index = 0
        var id = 0

        //Fill the array with numbers, and use -1 as the '.' space
        input.forEachIndexed { i, o ->
            if (i % 2 == 0) {
                repeat(o) { part1Nums[index++] = id }
                id++
            } else {
                index += o
            }
        }

        val part2Nums = part2(part1Nums)

        //Indexes based on sizing
        var left1 = 0
        var right1 = part1Nums.size - 1

        //Modify part1Nums - Move the rightmost number into the leftmost free spot.
        while (left1 < right1) {
            //Keep incrementing an index while -1 is not found (or '.' for the challenge)
            while (left1 < right1 && part1Nums[left1] != -1) left1++
            //Decrement right side, until a number is found
            while (left1 < right1 && part1Nums[right1] == -1) right1--
            //If we haven't overlapped index checks, swap locations found
            if (left1 < right1) {
                part1Nums[left1] = part1Nums[right1]
                part1Nums[right1] = -1
            }
        }

        //Loop through summing the Index * Value
        part1Nums
            .asSequence()
            .takeWhile { it != -1 }
            .forEachIndexed { i, c -> part1 += i * c }

        part2Nums.forEachIndexed { i, c ->
            if (c != -1) {
                part2 += i * c
            }
        }

        return Pair(part1, part2)
    }

    //Testing lines - If any fails, it throws an error
    val (test1, test2) = challenge(numberInput(9, test = true))
    "Test 1: $test1".println()
    "Test 2: $test2".println()
    check(test1 == 1928L)
    check(test2 == 2858L)

    //Runs part 1 and 2
    val (part1,part2) = challenge(numberInput(9))
    "Part 1: $part1".println()
    "Part 2: $part2".println()
}

fun part2(input: IntArray): List<Int> {
    val result = groupAdjacentNumbers(input.copyOf()) //Gets a grouping of all adjacent numbers
    val checked = mutableListOf<Int>() //Used to store checked right-most groups

    //Only do a single pass-through, so a while loop is unnecessary
    val maxSize = result.size
    repeat(maxSize){
        //Get last group of numbers not checked, and valid
        val lastGroup = result.indexOfLast {it[0] != -1 && !checked.contains(it[0]) }

        if (lastGroup != -1) {
            val lastSize = result[lastGroup].size

            //Get first group of free space that can fit the numbers
            val firstFree = result.indexOfFirst { it[0] == -1 && it.size >= lastSize }

            val numToReplace = result[lastGroup]

            //If the index of the free spot is further to the right than the group being checked, then it doesn't need moved
            if (firstFree >= lastGroup){
                checked.add(numToReplace[0])
            }else{
                //A space was found
                if (firstFree != -1) {
                    val firstGroup = result[firstFree]

                    //Replace the lastGroup location with a free space
                    result[lastGroup] = MutableList(lastSize){-1}

                    if (firstGroup.size > lastSize) {
                        //Reduce the free space by the nextSize amount, then insert the new number
                        result[firstFree] = MutableList(firstGroup.size - lastSize){-1}
                        result.add(firstFree, numToReplace)
                    } else {
                        //Replace the entire space
                        result[firstFree] = numToReplace
                    }
                }
                //If no space is valid (-1 was returned), we'll add the checked number to the checked list, to avoid a loop
                //We'll also add it to checked list, if valid, so that way it's not checked later
                checked.add(numToReplace[0])
            }
        }else{
            //We'll break early, as all nums have been checked once
            return result.flatten()
        }
    }

    return result.flatten()
}

fun groupAdjacentNumbers(numbers: IntArray): MutableList<MutableList<Int>> {
    return numbers.fold(mutableListOf()) { acc, num ->
        if (acc.isEmpty() || acc.last().last() != num) {
            acc.add(mutableListOf(num))
        } else {
            acc.last().add(num)
        }
        acc
    }
}