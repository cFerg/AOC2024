import Direction.Companion.modifyPos
import Direction.Companion.updateDirection

enum class Direction(val x: Int, val y: Int) {
    UP(0,-1),
    DOWN(0,1),
    LEFT(-1,0),
    RIGHT(1,0),
    DONE(0,0);

    companion object {
        fun Direction.modifyPos(pair: Pair<Int,Int>) = Pair(x + pair.first, y + pair.second)
        fun Direction.updateDirection() = when(this){
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
            else -> DONE
        }
    }
}

fun findMissingPairsToCompleteSquare(points: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val pointSet = points.toSet()
    val missingPoints = mutableSetOf<Pair<Int, Int>>()

    // Check all combinations of two points
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[j]

            // Check if the two points form a diagonal of a square
            if (x1 != x2 && y1 != y2) {
                // Calculate the other two corners of the square
                val potentialPoint1 = Pair(x1, y2)
                val potentialPoint2 = Pair(x2, y1)

                // Check if these two points exist in the set
                // I need one to exist, and the other to be potential
                if (potentialPoint1 !in pointSet) missingPoints.add(potentialPoint1)
                if (potentialPoint2 !in pointSet) missingPoints.add(potentialPoint2)
            }
        }
    }

    return missingPoints.toList()
}

fun main(){
    fun challenge(input: List<String>): Pair<Int,Int> {
        var xLimit = input[0].length - 1
        var yLimit = input.size - 1

        val obstacles = mutableListOf<Pair<Int,Int>>()
        var currentPos = Pair(0,0)
        var startingPos = Pair(0,0)
        var direction = Direction.UP

        for ((y,row) in input.withIndex()){
            for ((x,symbol) in row.withIndex()){
                when(symbol){
                    '.' -> {}
                    '#' -> {
                        //obstacle
                        obstacles.add(Pair(x,y))
                    }
                    else -> {
                        currentPos = Pair(x,y)
                        startingPos = Pair(x,y)
                        direction = when(symbol){
                            '^' -> Direction.UP
                            '>' -> Direction.RIGHT
                            '<' -> Direction.LEFT
                            'v' -> Direction.DOWN
                            else -> Direction.DONE
                        }
                    }
                }
            }
        }

        val potentialObjects = findMissingPairsToCompleteSquare(obstacles).toMutableList()
        val followedList = mutableSetOf<Pair<Int,Int>>()

        //Part 1 - Traverse out of the maze by rotating around objects
        while (direction != Direction.DONE){
            val newLocation = direction.modifyPos(currentPos)

            if (obstacles.contains(newLocation)){
                //Obstacle in the way, change direction
                direction = direction.updateDirection()
            }else {
                //Perform a step ahead
                followedList.add(currentPos)

                currentPos = direction.modifyPos(currentPos)
                val (currX,currY) = currentPos
                if (currX < 0 || currX > xLimit || currY < 0 || currY > yLimit){
                    direction = Direction.DONE
                }
            }
        }

        direction = Direction.UP
        var loopSize = 0

        while (direction != Direction.DONE) {
            var newPos = startingPos
            direction = Direction.UP
            val validObjects = mutableListOf<Pair<Int,Int>>()

            val newLocation = direction.modifyPos(newPos)

            if (obstacles.contains(newLocation)) {
                //Obstacle in the way, change direction
                direction = direction.updateDirection()
            } else if (potentialObjects.contains(newLocation)){
                //Treat same as an obstacle, but track twice for a loop
                if (validObjects.contains(newLocation)){
                    potentialObjects.remove(newLocation)
                    loopSize++
                    //Loop is hit, redo the search
                    continue
                }else {
                    validObjects.add(newLocation)
                }
                //If same location is hit, ie it's in valid objects, we'll delete it from potential
                //This allows us to check another loop
                direction = direction.updateDirection()
            } else {
                newPos = direction.modifyPos(newPos)
                val (currX, currY) = newPos
                if (currX < 0 || currX > xLimit || currY < 0 || currY > yLimit) {
                    direction = Direction.DONE
                }
            }
        }

        return Pair(followedList.size,loopSize)
    }

    //Testing lines - If any fails, it throws an error
    val test = stringInput(6, test = true)
    check(challenge(test).first == 41)
    check(challenge(test).second == 6)

    //Runs part 1 and 2
    val input = stringInput(6)
    challenge(input).first.println()
    challenge(input).second.println()
}