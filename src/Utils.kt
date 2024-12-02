import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun stringInput(name: String, grouped:Boolean = false, groupsSorted:Boolean = false) = Path("src/$name.txt").readText().trim().lines().fastMap{
    when {
        grouped -> it.split("\\s+".toRegex()).toMutableList().apply{if (groupsSorted) sort()}
        else -> it
    }
}

/**
 * Reads lines from the given input txt file, and groups them as Integers per line.
 */
fun numberInput(name: String, rowSorted:Boolean = false) = Path("src/$name.txt").readText().trim().lines().fastMap{
    row -> row.split("\\s+".toRegex()).fastMap{
        word -> word.toInt()
    }.toMutableList().apply {
        when {
            rowSorted -> sort()
        }
    }
}
/**
 * Reads lines from the given input txt file, and groups them vertically as Integers (Kept as a List to allow for destructuring)
 */
fun numberInputColumnGrouped(name: String, columnSorted:Boolean = false): MutableList<MutableList<Int>> {
    val lines = mutableListOf<MutableList<Int>>()

    Path("src/$name.txt").readText().trim().lines().fastForEach{ row ->
        row.split("\\s+".toRegex()).fastForEachIndexed { col, num ->
            lines.getOrNull(col)?.add(num.toInt()) ?: lines.add(mutableListOf(num.toInt()))
        }
    }

    when {
        columnSorted -> lines.fastForEach { it.sort() }
    }

    return lines
}

/**
 * Checks if a given list has the elements ordered in either direction
 */
fun <T: Comparable<T>> isOrdered(list: List<T>): Boolean {
    return (isAscending(list) || isDescending(list))
}

/**
 * Checks if a given list has elements increment
 */
fun <T: Comparable<T>> isAscending(list: List<T>): Boolean {
    if (list.isEmpty() || list.size == 1) return true

    return list.zipWithNext().all{it.first < it.second}
}

/**
 * Checks if a given list has elements decrement
 */
fun <T : Comparable<T>> isDescending(list: List<T>): Boolean {
    if (list.isEmpty() || list.size == 1) return true

    return list.zipWithNext().all{it.first > it.second}
}

/**
 * Added, as a way to drop at a specific index
 */
fun <T> List<T>.dropAt(index: Int) = this.filterIndexed { i, _ ->
    i != index
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

inline fun String.fastForEach(action: (Char) -> Unit){
    for (index in indices){
        val item = get(index)
        action(item)
    }
}

inline fun <T> Array<T>.fastForEach(action: (T) -> Unit){
    for (index in indices){
        val item = get(index)
        action(item)
    }
}

//TODO compare elementAt call vs passing as MutableList - Need to skip iterable overhead
/**
 * Iterates through a [Collection] using the index and calls [action] for each item.
 */
inline fun <T> Collection<T>.fastForEach(action: (T) -> Unit) {
    for (index in indices){
        action(elementAt(index))
    }
}

/**
 * Iterates through a [Map], by passing to Set - Then using the index, calls [action] for each item.
 */
inline fun <K,V> Map<K,V>.fastForEach(action: (K,V) -> Unit) {
    keys.fastForEach{
        action(it,getValue(it))
    }
}

/**
 * Iterates through a [List] using the index and calls [action] for each item.
 * This does not allocate an iterator like [Iterable.forEach].
 */
inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}

//TODO compare elementAt call vs passing as MutableList - Need to skip iterable overhead
/**
 * Iterates through a [Set] using the index and calls [action] for each item.
 */
inline fun <T> Set<T>.fastForEach(action: (T) -> Unit) {
    for (index in indices){
        action(elementAt(index))
    }
}

/**
 * Iterates through a [List] using the index and calls [action] for each item.
 * This does not allocate an iterator like [Iterable.forEachIndexed].
 */
inline fun <T> List<T>.fastForEachIndexed(action: (Int, T) -> Unit) {
    for (index in indices) {
        val item = get(index)
        action(index, item)
    }
}

/**
 * Returns `true` if at least one element matches the given [predicate].
 */
inline fun <T> List<T>.fastAny(predicate: (T) -> Boolean): Boolean {
    fastForEach { if (predicate(it)) return true }

    return false
}

/**
 * Returns `true` if at least one element matches the given [predicate].
 */
inline fun String.fastAny(predicate:(Char) -> Boolean): Boolean {
    this.toList().fastForEach{ if (predicate(it)) return true }

    return false
}

/**
 * Returns the first value that [predicate] returns `true` for or `null` if nothing matches.
 */
inline fun <T> List<T>.fastFirstOrNull(predicate: (T) -> Boolean): T? {
    fastForEach { if (predicate(it)) return it }

    return null
}

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the
 * list.
 */
inline fun <T> List<T>.fastSum(selector: (T) -> Int): Int {
    var sum = 0

    fastForEach { element ->
        sum += selector(element)
    }

    return sum
}

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the
 * list.
 */
inline fun <T> List<T>.fastSumLong(selector: (T) -> Int): Long {
    var sum = 0.toLong()

    fastForEach { element ->
        sum += selector(element)
    }

    return sum
}

/**
 * Returns a list containing the results of applying the given [transform] function
 * to each element in the original collection.
 */
inline fun <T, R> List<T>.fastMap(transform: (T) -> R): List<R> {
    val target = ArrayList<R>(size)

    fastForEach {
        target += transform(it)
    }

    return target
}

/**
 * Returns the first element yielding the largest value of the given function or `null` if there
 * are no elements.
 */
inline fun <T, R : Comparable<R>> List<T>.fastMaxBy(selector: (T) -> R): T? {
    if (isEmpty()) return null
    var maxElem = get(0)
    var maxValue = selector(maxElem)

    for (i in 1..lastIndex) {
        val e = get(i)
        val v = selector(e)

        if (maxValue < v) {
            maxElem = e
            maxValue = v
        }
    }

    return maxElem
}

/**
 * Applies the given [transform] function to each element of the original collection
 * and appends the results to the given [destination].
 */
inline fun <T, R, C : MutableCollection<in R>> List<T>.fastMapTo(destination: C, transform: (T) -> R): C {
    fastForEach { item ->
        destination.add(transform(item))
    }

    return destination
}