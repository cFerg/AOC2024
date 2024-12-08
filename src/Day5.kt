fun main(){
    fun challenge(input: List<String>): Pair<Long,Long> {
        //List of rules to do a lookup for later
        val rules = mutableListOf<Pair<Int,Int>>()
        val validRules = mutableListOf<Pair<Int,Int>>()
        //List to store update lists
        val correctList = mutableSetOf<MutableList<Int>>()
        val incorrectList = mutableSetOf<MutableList<Int>>()

        //Assigning loop to each list, initially
        var ruleSearch = true
        input.fastForEach { i ->
            if (i.isEmpty()){
                ruleSearch = false
                return@fastForEach
            }

            if (ruleSearch){
                val (a,b) = i.split("|")
                rules += a.toInt() to b.toInt()
            }else{
                correctList += i.split(",").map { it.toInt() }.toMutableList()
            }
        }

        //Loop to remove from correct list, if a rule doesn't match
        //Make a clone to avoid concurrent exception
        correctList.toMutableList().fastForEach { c ->
            rules.fastForEach { r ->
                if (c.contains(r.first) && c.contains(r.second)){
                    validRules.add(r)
                    if (c.indexOf(r.first) >= c.indexOf(r.second)){
                        correctList.remove(c)
                        incorrectList += c
                    }
                }
            }
        }

        val updatedList = incorrectList.toMutableList().fastMap { sortNumbersToMatchRules(it, validRules) }

        //Get the middle number from each list, and output them
        var firstOutput = 0L
        for (c in correctList){
            firstOutput += c.getOrNull(c.size / 2) ?: 0
        }

        var secondOutput = 0L
        for (u in updatedList){
            secondOutput += u.getOrNull(u.size / 2) ?: 0
        }

        return Pair(firstOutput,secondOutput)
    }

    //Testing lines - If any fails, it throws an error
    val test = stringInput(5, test = true)
    check(challenge(test).first == 143L)
    check(challenge(test).second == 123L)

    //Runs part 1 and 2
    val input = stringInput(5)
    challenge(input).first.println()
    challenge(input).second.println()
}

fun sortNumbersToMatchRules(numbers: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
    // Step 1: Create a directed graph and in-degree map based on the rules
    val adjacencyList = mutableMapOf<Int, MutableList<Int>>()
    val inDegree = mutableMapOf<Int, Int>().withDefault { 0 }

    // Initialize adjacency list and in-degree for all numbers in the list
    numbers.fastForEach { number ->
        adjacencyList[number] = mutableListOf()
        inDegree[number] = 0
    }

    // Populate adjacency list and in-degree from rules
    rules.fastForEach { (a, b) ->
        if (b in numbers && a in numbers) { // Ensure rules are only for numbers in the list
            adjacencyList[a]?.add(b)
            inDegree[b] = inDegree.getValue(b) + 1
        }
    }

    // Step 2: Topological sort while preserving the order of numbers not involved in rules
    val sortedList = mutableListOf<Int>()
    val queue = ArrayDeque<Int>()

    // Add all numbers with 0 in-degree to the queue, maintaining their order in the input
    numbers.filterTo(queue) { inDegree[it] == 0 }

    // Process the queue
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        sortedList.add(current)

        // Reduce the in-degree of neighbors and add them to the queue when their in-degree becomes 0
        (adjacencyList[current] ?: emptyList()).fastForEach {
            inDegree[it] = inDegree.getValue(it) - 1
            if (inDegree[it] == 0) {
                queue.add(it)
            }
        }
    }

    // Validate: If sortedList does not contain all numbers, a cycle or missing nodes are the issue
    if (sortedList.size != numbers.size) {
        throw IllegalArgumentException("Conflicting rules detected. Unable to sort.")
    }

    return sortedList
}