fun main(){
    fun challenge(grid: List<String>): Pair<Int,Int>{
        // Parse the input into a grid
        val rows = grid.size
        val cols = grid[0].length

        // Find all antennas and their positions
        val antennas = mutableListOf<Pair<Char, Pair<Int, Int>>>()
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val char = grid[r][c]
                if (char.isLetterOrDigit()) {
                    antennas.add(Pair(char, r to c))
                }
            }
        }

        // Calculate antinodes for part 1
        val antinodesPart1 = mutableSetOf<Pair<Int, Int>>()
        for (i in antennas.indices) {
            for (j in i + 1 until antennas.size) {
                val (freq1, pos1) = antennas[i]
                val (freq2, pos2) = antennas[j]
                if (freq1 == freq2) {
                    val (r1, c1) = pos1
                    val (r2, c2) = pos2

                    // Calculate antinodes based on the problem description
                    val dr = r2 - r1
                    val dc = c2 - c1

                    // Antinode closer to pos1
                    val antinode1 = r1 - dr to c1 - dc
                    if (isWithinBounds(antinode1.first, antinode1.second, rows, cols)) {
                        antinodesPart1.add(antinode1)
                    }

                    // Antinode farther from pos2
                    val antinode2 = r2 + dr to c2 + dc
                    if (isWithinBounds(antinode2.first, antinode2.second, rows, cols)) {
                        antinodesPart1.add(antinode2)
                    }
                }
            }
        }

        // Find all antennas and group by frequency
        val antennasByFrequency = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val char = grid[r][c]
                if (char.isLetterOrDigit()) {
                    antennasByFrequency.getOrPut(char) { mutableListOf() }.add(r to c)
                }
            }
        }

        // Calculate antinodes for part 2
        val antinodesPart2 = mutableSetOf<Pair<Int, Int>>()
        for ((_, antennas) in antennasByFrequency) {
            if (antennas.size < 2) continue

            // Add antenna positions as potential antinodes
            antinodesPart2.addAll(antennas)

            // Check every pair of antennas and find collinear points
            for (i in antennas.indices) {
                for (j in i + 1 until antennas.size) {
                    val (r1, c1) = antennas[i]
                    val (r2, c2) = antennas[j]

                    // Calculate the vector between two antennas
                    val dr = r2 - r1
                    val dc = c2 - c1

                    // Normalize the vector to its smallest step
                    val gcd = gcd(dr, dc)
                    val stepR = dr / gcd
                    val stepC = dc / gcd

                    // Generate all collinear points in both directions
                    var r = r1
                    var c = c1
                    while (isWithinBounds(r, c, rows, cols)) {
                        antinodesPart2.add(r to c)
                        r -= stepR
                        c -= stepC
                    }

                    r = r2
                    c = c2
                    while (isWithinBounds(r, c, rows, cols)) {
                        antinodesPart2.add(r to c)
                        r += stepR
                        c += stepC
                    }
                }
            }
        }

        return(Pair(antinodesPart1.size, antinodesPart2.size))
    }

    val (test1, test2) = challenge(stringInput(8,true))
    check(test1 == 14)
    check(test2 == 34)

    val (part1, part2) = challenge(stringInput(8))
    println("Part 1 - Unique antinode locations: $part1")
    println("Part 2 - Unique antinode locations: $part2")
}

//Helper function to compute the greatest common divisor
fun gcd(a: Int, b: Int): Int {
    if (b == 0) return kotlin.math.abs(a)
    return gcd(b, a % b)
}

//Check if a location is within the grid bounds
fun isWithinBounds(r: Int, c: Int, rows: Int, cols: Int): Boolean {
    return r in 0 until rows && c in 0 until cols
}