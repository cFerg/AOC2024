fun main(){
    fun part1(input: List<String>) = findWordsInGrid(input , "XMAS")
    fun part2(input: List<String>)= countSharedXPatterns(input)

    //Testing lines - If any fails, it throws an error
    val test = stringInput(4, test = true)
    check(part1(test) == 18)
    check(part2(test) == 9)

    //Runs part 1 and 2
    val input = stringInput(4)
    part1(input).println()
    part2(input).println()
}

fun findWordsInGrid(grid: List<String>, searchedWord: String): Int {
    val numRows = grid.size
    val numCols = grid[0].length
    var foundWords = 0

    // Directions for orthogonal and diagonal movements
    val directions = listOf(
        Pair(0, 1),   // Right
        Pair(1, 0),   // Down
        Pair(0, -1),  // Left
        Pair(-1, 0),  // Up
        Pair(1, 1),   // Diagonal: Down-Right
        Pair(1, -1),  // Diagonal: Down-Left
        Pair(-1, 1),  // Diagonal: Up-Right
        Pair(-1, -1)  // Diagonal: Up-Left
    )

    // Function to explore words from a starting point in a given direction
    fun exploreFrom(row: Int, col: Int, dx: Int, dy: Int) {
        var x = row
        var y = col
        val word = StringBuilder()

        while (x in 0 until numRows && y in 0 until numCols) {
            word.append(grid[x][y])
            val currentWord = word.toString()
            if (currentWord == searchedWord) {
                foundWords++
            }
            x += dx
            y += dy
        }
    }

    // Iterate through each cell in the grid
    for (row in 0 until numRows) {
        for (col in 0 until numCols) {
            // Explore all directions from the current cell
            for ((dx, dy) in directions) {
                exploreFrom(row, col, dx, dy)
            }
        }
    }

    return foundWords
}

fun countSharedXPatterns(grid: List<String>): Int {
    val numRows = grid.size
    val numCols = grid[0].length
    var count = 0

    // Helper function to check if a given center forms a valid X with intersecting diagonals
    fun isValidSharedX(x: Int, y: Int): Boolean {
        //Check if position is A, before assigning variables - helps skip steps
        if (grid[x][y] != 'A') return false

        // Diagonal positions
        val (topLeftX, topLeftY) = Pair(x - 1, y - 1)
        val (bottomRightX, bottomRightY) = Pair(x + 1, y + 1)

        //Check if diagonal forms MAS or SAM
        val diagonal1IsMAS = grid[topLeftX][topLeftY] == 'M' && grid[bottomRightX][bottomRightY] == 'S'
        val diagonal1IsSAM = grid[topLeftX][topLeftY] == 'S' && grid[bottomRightX][bottomRightY] == 'M'

        //Split the diagonals to avoid checking other side, if one is false
        if (diagonal1IsMAS || diagonal1IsSAM) {
            val (topRightX, topRightY) = Pair(x - 1, y + 1)
            val (bottomLeftX, bottomLeftY) = Pair(x + 1, y - 1)

            // Check if other diagonal forms MAS or SAM
            val diagonal2IsMAS = grid[topRightX][topRightY] == 'M' && grid[bottomLeftX][bottomLeftY] == 'S'
            val diagonal2IsSAM = grid[topRightX][topRightY] == 'S' && grid[bottomLeftX][bottomLeftY] == 'M'

            return diagonal2IsMAS || diagonal2IsSAM
        }

        return false
    }

    // Iterate through each cell in the grid, offset to avoid edges
    for (row in 1 until numRows - 1){
        for (col in 1 until numCols - 1){
            if (isValidSharedX(row, col)){
                count++
            }
        }
    }

    return count
}